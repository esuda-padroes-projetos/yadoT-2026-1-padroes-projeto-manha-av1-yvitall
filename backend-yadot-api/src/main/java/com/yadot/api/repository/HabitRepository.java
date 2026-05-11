package com.yadot.api.repository;

import com.yadot.api.enums.DiasSemana;
import com.yadot.api.model.HabitModel;
import com.yadot.api.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Essa interface herda todos os poderes de salvar, deletar e buscar do Spring com a função já pronta do JPA
@Repository
public interface HabitRepository extends JpaRepository<HabitModel, Long> {
    List<HabitModel> findByUsuario(UserModel usuario);
    List<HabitModel> findByUsuarioAndDiasSemanaContaining(UserModel usuario, DiasSemana dia);
}