package com.yadot.api.service;

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
    public HabitModel criarHabito(HabitModel novoHabito) {
        return habitRepository.save(novoHabito);
    }

    // Listar todos os hábitos de um usuário
    public List<HabitModel> listarPorUsuario(Long usuarioId) {
        // Primeiro buscamos o usuário — se não existir, lança erro
        UserModel usuario = userRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
        // orElseThrow é o Optional dizendo: "se tiver valor, retorna; se não, lança essa exceção"

        return habitRepository.findByUsuario(usuario);
    }

    // Listar hábitos do dia atual — base do gráfico circular
    public List<HabitModel> listarHabitosDoDia(Long usuarioId) {
        UserModel usuario = userRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        // Pega o dia da semana atual do Java (em inglês: MONDAY, TUESDAY...)
        DayOfWeek diaSemanaJava = LocalDate.now().getDayOfWeek();

        // Converte para o seu enum em português
        DiasSemana diaAtual = converterDia(diaSemanaJava);

        return habitRepository.findByUsuarioAndDiasDaSemanaContaining(usuario, diaAtual);
    }

    // Buscar hábito por ID — usado pelo lápis de edição
    public HabitModel buscarPorId(Long id) {
        return habitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hábito não encontrado."));
    }

    // Editar hábito
    public HabitModel editarHabito(Long id, HabitModel dadosAtualizados) {
        HabitModel habitoExistente = buscarPorId(id);

        // Atualiza só os campos que podem mudar
        habitoExistente.setHabitName(dadosAtualizados.getHabitName());
        habitoExistente.setCategoria(dadosAtualizados.getCategoria());
        habitoExistente.setHabitIcon(dadosAtualizados.getHabitIcon());
        habitoExistente.setDiasDaSemana(dadosAtualizados.getDiasDaSemana());

        return habitRepository.save(habitoExistente);
        // save() com ID existente faz UPDATE, não INSERT — o JPA sabe distinguir
    }

    // Deletar hábito
    public void deletarHabito(Long id) {
        HabitModel habito = buscarPorId(id); // garante que existe antes de deletar
        habitRepository.delete(habito);
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
}
