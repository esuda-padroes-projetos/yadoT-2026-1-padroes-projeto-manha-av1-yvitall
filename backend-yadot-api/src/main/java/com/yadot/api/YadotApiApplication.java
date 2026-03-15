package com.yadot.api;

import com.yadot.api.enums.DiasSemanaModel;
import com.yadot.api.model.CategoriaModel;
import com.yadot.api.model.HabitModel;
import com.yadot.api.repository.CategoriaRepository;
import com.yadot.api.repository.HabitRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class YadotApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(YadotApiApplication.class, args);
    }
}