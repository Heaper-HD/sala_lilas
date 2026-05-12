package com.fadergs.salalilas.backend.triage.technical.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "anamnese_tecnica_objetivos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnamneseTecnicaObjetivos {

    @EmbeddedId
    private AnamneseTecnicaObjetivoId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("anamneseTecnicaId")
    @JoinColumn(name = "anamnese_tecnica_id")
    private AnamneseTecnica anamneseTecnica;

    @Column(name = "objetivo_outro")
    private String objetivoOutro;

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnamneseTecnicaObjetivoId implements Serializable {

        @Column(name = "anamnese_tecnica_id")
        private UUID anamneseTecnicaId;

        @Column(name = "objetivo")
        private String objetivo;
    }
}