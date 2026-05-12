package com.fadergs.salalilas.backend.triage.initial.entity;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fadergs.salalilas.backend.appointment.entity.Agendamento;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "anamnese_inicial")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnamneseInicial {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agendamento_id", nullable = false, unique = true)
    private Agendamento agendamento;

    @Column(name = "tipo_atendimento", nullable = false)
    private String tipoAtendimento;

    @Column(name = "primeiro_atendimento", nullable = false)
    private boolean primeiroAtendimento;

    @Column(name = "territorio")
    private String territorio;

    @Column(name = "cor_raca", nullable = false)
    private String corRaca;

    @Column(name = "sexo_genero", nullable = false)
    private String sexoGenero;

    @Column(name = "sexo_genero_outro")
    private String sexoGeneroOutro;

    @OneToMany(
            mappedBy = "anamneseInicial",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @Builder.Default
    private List<AnamneseInicialViolencia> violencias = new ArrayList<>();

    @Column(name = "criado_em", nullable = false, updatable = false)
    private OffsetDateTime criadoEm;

    @PrePersist
    private void prePersist() {
        this.criadoEm = OffsetDateTime.now();
    }
}
