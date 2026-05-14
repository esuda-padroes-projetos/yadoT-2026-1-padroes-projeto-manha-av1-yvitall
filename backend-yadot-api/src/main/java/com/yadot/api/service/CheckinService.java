package com.yadot.api.service;

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

    public CheckinModel realizarCheckin(CheckinModel checkin) {
        // Regra: só permite checkin na data de hoje
        if (!checkin.getDataCheckin().equals(LocalDate.now())) {
            throw new RuntimeException("Não é possível fazer check-in em datas passadas.");
        }
        return checkinRepository.save(checkin);
    }

    public List<CheckinModel> historicoPorHabito(Long habitoId) {
        HabitModel habito = habitRepository.findById(habitoId)
                .orElseThrow(() -> new RuntimeException("Hábito não encontrado."));
        return checkinRepository.findByHabit(habito);
        // precisará adicionar findByHabit no CheckinRepository
    }

    public Map<String, Long> progressoDoDia(Long usuarioId) {
        UserModel usuario = userRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        List<CheckinModel> checkinsDoDia = checkinRepository
                .findByHabitUsuarioAndDataCheckin(usuario, LocalDate.now());

        long concluidos = checkinsDoDia.size();

        DiasSemana diaAtual = converterDia(LocalDate.now().getDayOfWeek());
        long totalDoDia = habitRepository
                .findByUsuarioAndDiasDaSemanaContaining(usuario, diaAtual).size();

        long pendentes = totalDoDia - concluidos;

        Map<String, Long> progresso = new HashMap<>();
        progresso.put("concluidos", concluidos);
        progresso.put("pendentes", Math.max(pendentes, 0));
        progresso.put("total", totalDoDia);
        return progresso;
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
}
