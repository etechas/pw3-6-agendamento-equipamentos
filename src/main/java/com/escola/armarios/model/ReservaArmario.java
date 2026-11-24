package com.escola.armarios.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/** Mapeia a tabela RES_ARMARIO (SQL Server). */
@Entity
@Table(name = "RES_ARMARIO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservaArmario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NR_ARMARIO")
    private String numeroArmario;

    @Column(name = "NM_ALUNO")
    private String nomeAluno;

    @Column(name = "DT_INICIO")
    private LocalDateTime dataInicio;

    @Column(name = "DT_FIM")
    private LocalDateTime dataFim;

    @Column(name = "ATIVO")
    private Boolean ativo;
}
