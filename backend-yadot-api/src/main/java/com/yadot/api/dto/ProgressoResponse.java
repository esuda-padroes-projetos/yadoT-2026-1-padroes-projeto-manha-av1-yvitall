package com.yadot.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProgressoResponse{
    private Long concluidos;
    private Long pendentes;
    private Long total;
}
