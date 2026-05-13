package com.yadot.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "tb_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false)
    private String nome;

    @Column (nullable = false)
    private String sobrenome;

    @JsonIgnore
    @Column (nullable = false)
    private String senhaHash;

    @Column (nullable = false, unique = true)
    @NotNull
    @Email
    private String email;

    @JsonIgnore
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<HabitModel> habitos;
}