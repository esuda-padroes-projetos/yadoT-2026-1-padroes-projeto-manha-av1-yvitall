package com.yadot.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioCadastroRequest {
    private String nome;
    private String sobrenome;
    private String email;
    private String senhaHash;
}
