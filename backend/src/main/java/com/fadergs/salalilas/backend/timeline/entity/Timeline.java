package com.fadergs.salalilas.backend.timeline.entity;

import com.fadergs.salalilas.backend.appointment.entity.Agendamento;
import com.fadergs.salalilas.backend.common.enums.TipoEventoTimeline;
import com.fadergs.salalilas.backend.user.entity.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "timeline")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Timeline {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agendamento_id", nullable = false)
    private Agendamento agendamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "evento", columnDefinition = "tipo_evento_timeline")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private TipoEventoTimeline evento;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "criado_por")
    private Usuario criadoPor;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private OffsetDateTime criadoEm;

    @PrePersist
    private void prePersist() {
        this.criadoEm = OffsetDateTime.now();
    }
}
