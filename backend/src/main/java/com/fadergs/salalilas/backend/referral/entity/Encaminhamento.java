package com.fadergs.salalilas.backend.referral.entity;

import com.fadergs.salalilas.backend.appointment.entity.Agendamento;
import com.fadergs.salalilas.backend.common.enums.PerfilUsuario;
import com.fadergs.salalilas.backend.user.entity.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "encaminhamentos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Encaminhamento {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agendamento_id", nullable = false)
    private Agendamento agendamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "origem_perfil", nullable = false)
    private PerfilUsuario origemPerfil;

    @Enumerated(EnumType.STRING)
    @Column(name = "destino_perfil")
    private PerfilUsuario destinoPerfil;

    @Column(name = "destino_outros", nullable = false)
    @Builder.Default
    private boolean destinoOutros = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "criado_por", nullable = false)
    private Usuario criadoPor;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private OffsetDateTime criadoEm;

    @PrePersist
    private void prePersist() {
        this.criadoEm = OffsetDateTime.now();
    }
}

