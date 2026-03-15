package com.yadot.api.model;

import com.yadot.api.enums.DiasSemanaModel;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data // Lombok
@Entity // Transforma a classe em uma entidade no banco de dados (necessario em classesModel/Entities)
@Table(name = "tb_habitos") //criacao de uma tabela com nome da tabela
@NoArgsConstructor
@AllArgsConstructor
public class HabitModel {
    @Id //add auto na tabela o id (dependency JavaPersistenceAPI/BD)
    @GeneratedValue(strategy = GenerationType.IDENTITY) //forma de gerar o id (1, 2...)

    private Long habitId;

    @Column(length = 50, nullable = false)
    private String habitName;

    // muitos hábitos podem ter a mesma categoria
    @ManyToOne
    @JoinColumn(name = "categoria_id") // precisamos juntar as duas tabelas (FK)
    private CategoriaModel categoria;

    @Column(nullable = false)
    private String habitIcon;

    private boolean saveGoogleCalendar;

    @ElementCollection(targetClass = DiasSemanaModel.class)
    @Enumerated(EnumType.STRING) // ao inves de armazenar os numeros(indices do enum) salvaremos a string contendo dia da semana
    @CollectionTable(name = "habitos_dias", joinColumns = @JoinColumn(name = "habit_id"))
    private List<DiasSemanaModel> diasDaSemana;

}
