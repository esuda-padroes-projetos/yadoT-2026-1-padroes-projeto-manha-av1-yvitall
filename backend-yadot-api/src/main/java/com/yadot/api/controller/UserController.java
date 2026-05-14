package com.yadot.api.controller;

import com.yadot.api.model.UserModel;
import com.yadot.api.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/cadastro")
    public ResponseEntity<String> cadastrar(@RequestBody UserModel novoUsuario){
        userService.registerUser(novoUsuario);
        return ResponseEntity.status(201).body("Usuário cadastrado com sucesso!");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserModel usuarioRequest){
        userService.loginUser(usuarioRequest);
        return ResponseEntity.ok("Login realizado com sucesso!");
    }

    @GetMapping
    public ResponseEntity<List<UserModel>> listarTodos(){
        return ResponseEntity.ok(userService.findAll());
    }
}
