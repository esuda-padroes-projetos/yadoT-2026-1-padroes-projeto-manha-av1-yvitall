package com.yadot.api.repository;

import com.yadot.api.model.CheckinModel;
import com.yadot.api.model.HabitModel;
import com.yadot.api.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CheckinRepository extends JpaRepository<CheckinModel, Long> {
    List<CheckinModel> findByHabit(HabitModel habit);
    List<CheckinModel> findByHabitUsuarioAndDataCheckin(UserModel usuario, LocalDate data);
}
