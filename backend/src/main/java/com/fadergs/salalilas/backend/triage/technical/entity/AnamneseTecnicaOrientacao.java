package com.fadergs.salalilas.backend.triage.technical.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "anamnese_tecnica_orientacoes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnamneseTecnicaOrientacao {

    @EmbeddedId
    private AnamneseTecnicaOrientacaoId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("anamneseTecnicaId")
    @JoinColumn(name = "anamnese_tecnica_id")
    private AnamneseTecnica anamneseTecnica;

    @Column(name = "orientacao_outro")
    private String orientacaoOutro;

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnamneseTecnicaOrientacaoId implements Serializable {

        @Column(name = "anamnese_tecnica_id")
        private UUID anamneseTecnicaId;

        @Column(name = "orientacao")
        private String orientacao;
    }
}
