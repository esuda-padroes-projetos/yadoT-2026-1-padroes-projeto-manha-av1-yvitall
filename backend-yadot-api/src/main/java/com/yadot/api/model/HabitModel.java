package com.yadot.api.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.yadot.api.enums.Categoria;
import com.yadot.api.enums.DiasSemana;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity // Transforma a classe em uma entidade no banco de dados (necessario em
        // classesModel/Entities)
@Table(name = "tb_habitos") // criacao de uma tabela com nome da tabela
@NoArgsConstructor
@AllArgsConstructor
public class HabitModel {
    @Id // add auto na tabela o id (dependency JavaPersistenceAPI/BD)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // forma de gerar o id (1, 2...)
    private Long habitId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private UserModel usuario;

    @Size(min = 3, max = 50)
    @Column(nullable = false, length = 50)
    private String habitName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Categoria categoria;

    @Column(nullable = false)
    private String habitIcon;

    @ElementCollection(targetClass = DiasSemana.class)
    @CollectionTable(name = "habitos_dias", joinColumns = @JoinColumn(name = "habit_id"))
    @Enumerated(EnumType.STRING) // ao inves de armazenar os numeros(indices do enum) salvaremos a string
                                 // contendo dia da semana
    private Set<DiasSemana> diasDaSemana = new HashSet<>();

    // Um Hábito tem um histórico de vários check-ins
    @OneToMany(mappedBy = "habit", cascade = CascadeType.ALL)
    private List<CheckinModel> historicoCheckins;
}

// aqui eu crio os moldes dos objetos que existirão nos programas, também
// definindo o que pode ou não pode ser feito
