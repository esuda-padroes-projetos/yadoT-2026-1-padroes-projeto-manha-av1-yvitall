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
        //encode is criptography function do BCrypt => a gente cria a variavel local, chama a função para criptografar e passa a senha atual para ser criptografada com get
        // encode() transforma "minhasenha123" em "$2a$10$..." (hash BCrypt) depois setamos a senhaCriptografada para o banco através do set
        novoUsuario.setSenhaHash(senhaCriptografada);

        userRepository.save(novoUsuario);
    }

    public void loginUser(UserModel usuarioRequest){
        UserModel usuarioBanco = userRepository.findByEmail(usuarioRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Email não cadastrado. Registre-se"));
        boolean senhaConfere = passwordEncoder.matches(
                usuarioRequest.getSenhaHash(),
                usuarioBanco.getSenhaHash());

        if (!senhaConfere) {
            throw new RuntimeException("Senha incorreta.");
        }
    }

    public Optional<UserModel> findByEmail(String email) { // Busca por email — será usado no login depois
        return userRepository.findByEmail(email);
    }
}

//frameworks é uma caralhada de ferramentas/funções já pré-prontas que ajudam a agilizar o trabalho e poupar código