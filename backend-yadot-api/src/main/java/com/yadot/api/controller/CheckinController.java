package com.yadot.api.controller;

import com.yadot.api.model.CheckinModel;
import com.yadot.api.service.CheckinService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/checkins")
public class CheckinController {

    private final CheckinService checkinService;

    public CheckinController(CheckinService checkinService) {
        this.checkinService = checkinService;
    }

    // POST /checkins — realiza check-in de um hábito
    @PostMapping
    public ResponseEntity<CheckinModel> realizarCheckin(@RequestBody CheckinModel checkin) {
        return ResponseEntity.status(201).body(checkinService.realizarCheckin(checkin));
    }

    // GET /checkins/habito/{habitoId} — histórico de checkins de um hábito
    @GetMapping("/habito/{habitoId}")
    public ResponseEntity<List<CheckinModel>> historicoPorHabito(@PathVariable Long habitoId) {
        return ResponseEntity.ok(checkinService.historicoPorHabito(habitoId));
    }

    // GET /checkins/progresso/{usuarioId} — dados para o gráfico circular
    @GetMapping("/progresso/{usuarioId}")
    public ResponseEntity<Map<String, Long>> progressoDoDia(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(checkinService.progressoDoDia(usuarioId));
    }
}
