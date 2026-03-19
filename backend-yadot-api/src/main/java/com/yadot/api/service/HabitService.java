package com.yadot.api.service;

import com.yadot.api.model.HabitModel;
import com.yadot.api.repository.CategoriaRepository;
import com.yadot.api.repository.HabitRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HabitService {
    private final HabitRepository habitRepository;
    private final CategoriaRepository categoriaRepository;
    public HabitService(HabitRepository habitRepository, CategoriaRepository categoriaRepository) {
        this.habitRepository = habitRepository;
        this.categoriaRepository = categoriaRepository;
    }
    public List<HabitModel> getAll() {
        return habitRepository.findAll();
    }

    public HabitModel save(HabitModel newHabit) {
        Long idCategoria = categoriaRepository.findById(newHabit.getCategoria().getId());
            if (categoriaRepository.findById(idCategoria).isEmpty()){
                throw new RuntimeException("Categoria não localizada.");
        }
                return habitRepository.save(newHabit);
    }
    public void deleteHabit(Long id){habitRepository.deleteById(id);}

}
