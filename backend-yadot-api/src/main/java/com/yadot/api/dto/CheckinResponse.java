package com.yadot.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckinResponse {
    @NotNull
    private Long habitId;
    @NotNull
    private Long id;

    private LocalDate hoje;


}
