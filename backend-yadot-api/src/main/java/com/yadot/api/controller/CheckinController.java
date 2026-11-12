package com.yadot.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yadot.api.dto.CheckinRequest;
import com.yadot.api.dto.CheckinResponse;
import com.yadot.api.dto.ProgressoResponse;
import com.yadot.api.service.CheckinService;

@RestController
@RequestMapping("/checkins")
public class CheckinController {
    private final CheckinService checkinService;

    public CheckinController(CheckinService checkinService) {
        this.checkinService = checkinService;
    }

    @PostMapping
    public ResponseEntity<CheckinResponse> realizarCheckin(@RequestBody CheckinRequest request) {
        return ResponseEntity.status(201).body(checkinService.realizarCheckin(request));
    }

    @GetMapping("/habito/{habitoId}")
    public ResponseEntity<List<CheckinResponse>> historico(@PathVariable Long habitoId) {
        return ResponseEntity.ok(checkinService.historicoPorHabito(habitoId));
    }

    @GetMapping("/progresso/{usuarioId}")
    public ResponseEntity<ProgressoResponse> progresso(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(checkinService.progressoDoDia(usuarioId));
    }
}