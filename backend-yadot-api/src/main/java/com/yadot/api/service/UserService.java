package com.yadot.api.service;

import com.yadot.api.dto.UsuarioCadastroRequest;
import com.yadot.api.dto.UsuarioLoginRequest;
import com.yadot.api.dto.UsuarioResponse;
import com.yadot.api.model.UserModel;
import com.yadot.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserModel> findAll() {
        return userRepository.findAll();
    }

    public UsuarioResponse registerUser(UsuarioCadastroRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email já cadastrado.");
        }
        UserModel novo = new UserModel();
        novo.setNome(request.getNome());
        novo.setSobrenome(request.getSobrenome());
        novo.setEmail(request.getEmail());
        novo.setSenhaHash(passwordEncoder.encode(request.getSenhaHash()));
        UserModel salvo = userRepository.save(novo);
        return toResponse(salvo);
    }

    public UsuarioResponse loginUser(UsuarioLoginRequest request) {
        UserModel usuario = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email não cadastrado."));
        if (!passwordEncoder.matches(request.getSenhaHash(), usuario.getSenhaHash())) {
            throw new RuntimeException("Senha incorreta.");
        }
        return toResponse(usuario);
    }

    // método privado para não repetir a conversão
    private UsuarioResponse toResponse(UserModel model) {
        UsuarioResponse response = new UsuarioResponse();
        response.setId(model.getId());
        response.setNome(model.getNome());
        response.setSobrenome(model.getSobrenome());
        response.setEmail(model.getEmail());
        return response;
    }

    public Optional<UserModel> findByEmail(String email) { // Busca por email — será usado no login depois
        return userRepository.findByEmail(email);
    }
}

//frameworks é uma caralhada de ferramentas/funções já pré-prontas que ajudam a agilizar o trabalho e poupar código