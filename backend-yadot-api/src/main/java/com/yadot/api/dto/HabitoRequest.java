package com.yadot.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HabitoRequest {
    private Long usuarioId;
    private String habitName;
    private String categoria;
    private String habitIcon;
    private List<String> diasDaSemana;
}
