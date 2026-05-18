package com.yadot.api.controller;

import com.yadot.api.dto.HabitoRequest;
import com.yadot.api.dto.HabitoResponse;
import com.yadot.api.model.HabitModel;
import com.yadot.api.service.HabitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/habitos")
public class HabitController {

    private final HabitService habitService;

    public HabitController(HabitService habitService) {
        this.habitService = habitService;
    }

    @PostMapping
    public ResponseEntity<HabitoResponse> criar(@RequestBody HabitoRequest request) {
        HabitoResponse response = habitService.criarHabito(request);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<List<HabitoResponse>> listarPorUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(habitService.listarPorUsuario(id));
    }

    @GetMapping("/hoje/{usuarioId}")
    public ResponseEntity<List<HabitoResponse>> listarHoje(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(habitService.listarHabitosDoDia(usuarioId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HabitoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(habitService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HabitoResponse> editar(@PathVariable Long id, @RequestBody HabitoRequest request) {
        return ResponseEntity.ok(habitService.editarHabito(id, request));
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        habitService.deletarHabito(id);
    }
}
