package br.edu.fadergs.salalilas.model;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

import br.edu.fadergs.salalilas.model.enums.StatusAgendamento;
import br.edu.fadergs.salalilas.model.enums.TipoAgendemento;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "agendamento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Agendamento {

    @Id
    @GeneratedValue
    @Column(name = "agendamento_id", updatable = false, nullable = false)
    private UUID agendamentoId;

    @Column(name = "nome_solicitante", nullable = false, length = 100)
    private String nomeSolicitante;

    @Column(name = "telefone_solicitante", nullable = false, length = 100)
    private String telefoneSolicitante;

    @Column(name = "email_solicitante", length = 100)
    private String emailSolicitante;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoAgendemento tipo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private StatusAgendamento status = StatusAgendamento.PENDENTE;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_responsavel_id")
    private Usuario usuarioResponsavel;

    @Generated(event = EventType.INSERT)
    @Column(name = "created_at", updatable = false, nullable = false)
    private OffsetDateTime createdAt;

    @Generated(event = {EventType.INSERT, EventType.UPDATE})
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}