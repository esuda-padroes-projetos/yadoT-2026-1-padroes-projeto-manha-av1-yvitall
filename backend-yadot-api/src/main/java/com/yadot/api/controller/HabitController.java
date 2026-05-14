package com.yadot.api.controller;

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

    @PostMapping("/create")
    public ResponseEntity<HabitModel> criar(@RequestBody HabitModel novoHabito) {
        HabitModel salvo = habitService.criarHabito(novoHabito);
        return ResponseEntity.status(201).body(salvo);
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<List<HabitModel>> listarHabitosPorUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(habitService.listarPorUsuario(id));
    }

    @GetMapping("/hoje/{usuarioId}")
    public ResponseEntity<List<HabitModel>> listarHojeDoUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(habitService.listarHabitosDoDia(usuarioId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HabitModel> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(habitService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HabitModel> editar(@PathVariable Long id, @RequestBody HabitModel dadosAtualizados) {
        return ResponseEntity.ok(habitService.editarHabito(id, dadosAtualizados));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        habitService.deletarHabito(id);
        return ResponseEntity.noContent().build();
    }
}