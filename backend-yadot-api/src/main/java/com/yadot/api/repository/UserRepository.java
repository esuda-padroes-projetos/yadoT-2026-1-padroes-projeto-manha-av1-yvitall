package com.yadot.api.repository;

import com.yadot.api.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
// Essa interface herda todos os poderes de salvar, deletar e buscar do Spring com a função já pronta do JPA
public interface UserRepository extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findByEmail(String email);
}