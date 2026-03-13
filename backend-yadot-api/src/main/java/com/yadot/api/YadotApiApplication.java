package com.yadot.api;

import com.yadot.api.model.HabitModel;
import com.yadot.api.repository.HabitRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class YadotApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(YadotApiApplication.class, args);
    }

    // ESSE BLOCO RODA SOZINHO ASSIM QUE O SERVIDOR LIGAR
    @Bean
    public CommandLineRunner testarBancoDeDados(HabitRepository habitRepository) {
        return args -> {
            System.out.println("🚀 INICIANDO TESTE DO BANCO DE DADOS...");

            // Cria um hábito no Java
            HabitModel meuPrimeiroHabito = new HabitModel();
            meuPrimeiroHabito.setHabitName("Beber 2L de Água");
            meuPrimeiroHabito.setHabitIcon("💧");
            meuPrimeiroHabito.setSaveGoogleCalendar(true);

            // O HIBERNATE TRADUZ PRA SQL E SALVA NO SUPABASE AQUI!
            habitRepository.save(meuPrimeiroHabito);

            System.out.println("✅ HÁBITO SALVO COM SUCESSO NO SUPABASE!");
        };
    }
}