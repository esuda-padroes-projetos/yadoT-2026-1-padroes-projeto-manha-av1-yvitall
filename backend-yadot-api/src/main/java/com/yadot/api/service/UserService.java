package com.yadot.api.service;

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

    public void registerUser(UserModel novoUsuario) {
        if (userRepository.findByEmail(novoUsuario.getEmail()).isPresent()) { //findByEmail retorna Optional — .isPresent() checa se encontrou algo
            throw new RuntimeException("Email já cadastrado.");
        }
        String senhaCriptografada = passwordEncoder.encode(novoUsuario.getSenhaHash());
        //encode is criptography function do BCrypt => a gente cria a variavel local, chama a função para criptografar e passa a senha atual com get
        // encode() transforma "minhasenha123" em "$2a$10$..." (hash BCrypt)
        novoUsuario.setSenhaHash(senhaCriptografada);

        userRepository.save(novoUsuario);
    }

    public void loginUser(UserModel usuario){
        if (userRepository.findByEmail(usuario.getEmail()).isPresent()){
            throw new RuntimeException("Email não cadastrado. Registre-se");
        }
        String senha = passwordEncoder.matches(usuario.senha, usuario.getSenhaHash()); //o que há de errado?
    }
    // Busca por email — será usado no login depois
    public Optional<UserModel> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}