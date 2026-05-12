package com.fadergs.salalilas.backend.triage.technical.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "anamnese_tecnica_encaminhamentos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnamneseTecnicaEncaminhamento {

    @EmbeddedId
    private AnamneseTecnicaEncaminhamentoId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("anamneseTecnicaId")
    @JoinColumn(name = "anamnese_tecnica_id")
    private AnamneseTecnica anamneseTecnica;

    @Column(name = "encaminhamento_outro")
    private String encaminhamentoOutro;

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnamneseTecnicaEncaminhamentoId implements Serializable {

        @Column(name = "anamnese_tecnica_id")
        private UUID anamneseTecnicaId;

        @Column(name = "encaminhamento")
        private String encaminhamento;
    }
}