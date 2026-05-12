package com.fadergs.salalilas.backend.triage.initial.entity;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "anamnese_inicial_violencias")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnamneseInicialViolencia {
    @EmbeddedId
    private AnamneseInicialViolenciaId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("anamneseInicialId")
    @JoinColumn(name = "anamnese_inicial_id")
    private AnamneseInicial anamneseInicial;

    @Column(name = "violencia_outro")
    private String violenciaOutro;

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnamneseInicialViolenciaId implements Serializable {
        @Column(name = "anamnese_inicial_id")
        private UUID anamneseInicialId;

        @Column(name = "violencia")
        private String violencia;
    }
}
