package com.fadergs.salalilas.backend.triage.technical.entity;

import com.fadergs.salalilas.backend.appointment.entity.Agendamento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "anamnese_tecnica")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnamneseTecnica {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agendamento_id", nullable = false, unique = true)
    private Agendamento agendamento;

    @Column(name = "risco_iminente", nullable = false)
    private boolean riscoIminente;

    @Column(name = "agressor_convive", nullable = false)
    private boolean agressorConvive;

    @Column(name = "historico_violencia", nullable = false)
    private boolean historicoViolencia;

    @Column(name = "rede_apoio", nullable = false)
    private boolean redeApoio;

    @Column(name = "filhos_dependentes", nullable = false)
    private boolean filhosDependentes;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "registro_atendimento", columnDefinition = "TEXT")
    private String registroAtendimento;

    @Column(name = "detalhamento_encaminhamentos", columnDefinition = "TEXT")
    private String detalhamentoEncaminhamentos;

    @Column(name = "plano_acompanhamento", nullable = false)
    private String planoAcompanhamento;

    @Column(name = "data_retorno")
    private LocalDate dataRetorno;

    @Column(name = "plano_observacoes", columnDefinition = "TEXT")
    private String planoObservacoes;

    @Column(name = "sintese_caso", columnDefinition = "TEXT")
    private String sinteseCaso;

    @OneToMany(
            mappedBy = "anamneseTecnica",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @Builder.Default
    private List<AnamneseTecnicaOrientacao> orientacoes = new ArrayList<>();

    @OneToMany(
            mappedBy = "anamneseTecnica",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    @Builder.Default
    private List<AnamneseTecnicaEncaminhamento> encaminhamentos = new ArrayList<>();

    @OneToMany(
            mappedBy = "anamneseTecnica",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @Builder.Default
    private List<AnamneseTecnicaObjetivos> objetivos = new ArrayList<>();

    @Column(name = "criado_em", nullable = false, updatable = false)
    private OffsetDateTime criadoEm;

    @PrePersist
    private void prePersist() {
        this.criadoEm = OffsetDateTime.now();
    }
}
