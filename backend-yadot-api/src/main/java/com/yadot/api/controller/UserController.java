package com.yadot.api.controller;

import com.yadot.api.dto.UsuarioCadastroRequest;
import com.yadot.api.dto.UsuarioLoginRequest;
import com.yadot.api.dto.UsuarioResponse;
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
    public ResponseEntity<UsuarioResponse> cadastrar(@RequestBody UsuarioCadastroRequest request) {
        UsuarioResponse response = userService.registerUser(request);
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioResponse> login(@RequestBody UsuarioLoginRequest usuarioRequest){
        UsuarioResponse response = userService.loginUser(usuarioRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<UserModel>> listarTodos(){
        return ResponseEntity.ok(userService.findAll());
    }
}
