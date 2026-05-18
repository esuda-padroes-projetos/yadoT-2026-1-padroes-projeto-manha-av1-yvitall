package com.yadot.api.service;

import com.yadot.api.dto.HabitoRequest;
import com.yadot.api.dto.HabitoResponse;
import com.yadot.api.enums.Categoria;
import com.yadot.api.enums.DiasSemana;
import com.yadot.api.model.HabitModel;
import com.yadot.api.model.UserModel;
import com.yadot.api.repository.HabitRepository;
import com.yadot.api.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
public class HabitService {

    private final HabitRepository habitRepository;
    private final UserRepository userRepository;

    public HabitService(HabitRepository habitRepository, UserRepository userRepository) {
        this.habitRepository = habitRepository;
        this.userRepository = userRepository;
    }

    // Criar hábito
    public HabitoResponse criarHabito(HabitoRequest request) {
        UserModel usuario = userRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
        HabitModel novo = new HabitModel();
        novo.setUsuario(usuario);
        novo.setHabitName(request.getHabitName());
        novo.setCategoria(Categoria.valueOf(request.getCategoria()));
        novo.setHabitIcon(request.getHabitIcon());
        novo.setDiasDaSemana(request.getDiasDaSemana().stream()
                .map(DiasSemana::valueOf)
                .toList());
        return toResponse(habitRepository.save(novo));
    }

    // Listar todos os hábitos de um usuário
    public List<HabitoResponse> listarPorUsuario(Long usuarioId) {
        UserModel usuario = userRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
        return habitRepository.findByUsuario(usuario)
                .stream().map(this::toResponse).toList();
    }

    // Listar hábitos do dia atual — base do gráfico circular
    public List<HabitoResponse> listarHabitosDoDia(Long usuarioId) {
        UserModel usuario = userRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
        DiasSemana diaAtual = converterDia(LocalDate.now().getDayOfWeek());
        return habitRepository.findByUsuarioAndDiasDaSemanaContaining(usuario, diaAtual)
                .stream().map(this::toResponse).toList();
    }

    // Buscar hábito por ID — usado pelo lápis de edição
    public HabitoResponse buscarPorId(Long id) {
        return toResponse(habitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hábito não encontrado.")));
    }

    // Editar hábito
    public HabitoResponse editarHabito(Long id, HabitoRequest request) {
        HabitModel existente = habitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hábito não encontrado."));
        existente.setHabitName(request.getHabitName());
        existente.setCategoria(Categoria.valueOf(request.getCategoria()));
        existente.setHabitIcon(request.getHabitIcon());
        existente.setDiasDaSemana(request.getDiasDaSemana().stream()
                .map(DiasSemana::valueOf).toList());
        return toResponse(habitRepository.save(existente));
    }

    // Deletar hábito
    public void deletarHabito(Long id) {
        habitRepository.delete(habitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hábito não encontrado.")));
    }

    // Conversão do DayOfWeek do Java para o seu enum DiasSemana
    private DiasSemana converterDia(DayOfWeek dia) {
        return switch (dia) {
            case MONDAY -> DiasSemana.SEGUNDA;
            case TUESDAY -> DiasSemana.TERCA;
            case WEDNESDAY -> DiasSemana.QUARTA;
            case THURSDAY -> DiasSemana.QUINTA;
            case FRIDAY -> DiasSemana.SEXTA;
            case SATURDAY -> DiasSemana.SABADO;
            case SUNDAY -> DiasSemana.DOMINGO;
        };
    }
    private HabitoResponse toResponse(HabitModel model) {
        HabitoResponse response = new HabitoResponse();
        response.setHabitId(model.getHabitId());
        response.setHabitName(model.getHabitName());
        response.setCategoria(model.getCategoria().name());
        response.setHabitIcon(model.getHabitIcon());
        response.setDiasDaSemana(model.getDiasDaSemana().stream()
                .map(DiasSemana::name).toList());
        return response;
    }
}
