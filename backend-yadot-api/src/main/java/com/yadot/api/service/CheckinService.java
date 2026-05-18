package com.yadot.api.service;

import com.yadot.api.dto.CheckinRequest;
import com.yadot.api.dto.CheckinResponse;
import com.yadot.api.dto.ProgressoResponse;
import com.yadot.api.enums.DiasSemana;
import com.yadot.api.model.CheckinModel;
import com.yadot.api.model.HabitModel;
import com.yadot.api.model.UserModel;
import com.yadot.api.repository.CheckinRepository;
import com.yadot.api.repository.HabitRepository;
import com.yadot.api.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CheckinService {

    private final CheckinRepository checkinRepository;
    private final HabitRepository habitRepository;

    private final UserRepository userRepository;

    public CheckinService(CheckinRepository checkinRepository, HabitRepository habitRepository, UserRepository userRepository) {
        this.checkinRepository = checkinRepository;
        this.habitRepository = habitRepository;
        this.userRepository = userRepository;
    }

    public CheckinResponse realizarCheckin(CheckinRequest request) {
        LocalDate data = LocalDate.parse(request.getDataCheckin());
        if (!data.equals(LocalDate.now())) {
            throw new RuntimeException("Não é possível fazer check-in em datas passadas.");
        }

        HabitModel habit = habitRepository.findById(request.getHabitId())
                .orElseThrow(() -> new RuntimeException("Hábito não encontrado."));
        CheckinModel checkin = new CheckinModel();
        checkin.setHabit(habit);
        checkin.setDataCheckin(data);
        return toResponse(checkinRepository.save(checkin));
    }

    public List<CheckinResponse> historicoPorHabito(Long habitoId) {
        HabitModel habit = habitRepository.findById(habitoId)
                .orElseThrow(() -> new RuntimeException("Hábito não encontrado."));
        return checkinRepository.findByHabit(habit)
                .stream().map(this::toResponse).toList();
    }

    public ProgressoResponse progressoDoDia(Long usuarioId) {
        UserModel usuario = userRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
        List<CheckinModel> checkinsDoDia = checkinRepository
                .findByHabitUsuarioAndDataCheckin(usuario, LocalDate.now());
        DiasSemana diaAtual = converterDia(LocalDate.now().getDayOfWeek());
        long total = habitRepository.findByUsuarioAndDiasDaSemanaContaining(usuario, diaAtual).size();
        long concluidos = checkinsDoDia.size();
        ProgressoResponse response = new ProgressoResponse();
        response.setConcluidos(concluidos);
        response.setPendentes(Math.max(total - concluidos, 0));
        response.setTotal(total);
        return response;
    }

    private DiasSemana converterDia(DayOfWeek dia) {
        return switch (dia) {
            case MONDAY    -> DiasSemana.SEGUNDA;
            case TUESDAY   -> DiasSemana.TERCA;
            case WEDNESDAY -> DiasSemana.QUARTA;
            case THURSDAY  -> DiasSemana.QUINTA;
            case FRIDAY    -> DiasSemana.SEXTA;
            case SATURDAY  -> DiasSemana.SABADO;
            case SUNDAY    -> DiasSemana.DOMINGO;
        };
    }
    private CheckinResponse toResponse(CheckinModel model) {
        CheckinResponse response = new CheckinResponse();
        response.setCheckinId(model.getCheckinId());
        response.setHabitId(model.getHabit().getHabitId());
        response.setDataCheckin(model.getDataCheckin().toString());
        return response;
    }
}
