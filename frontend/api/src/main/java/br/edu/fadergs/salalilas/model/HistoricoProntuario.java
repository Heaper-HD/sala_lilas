package br.edu.fadergs.salalilas.model;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.generator.EventType;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "historico_prontuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoricoProntuario {

    @Id
    @GeneratedValue
    @Column(name = "historico_id", updatable = false, nullable = false)
    private UUID historicoId;

    @ManyToOne
    @JoinColumn(name = "prontuario_id")
    private Prontuario prontuario;

    @ManyToOne
    @JoinColumn(name = "usuario_id")    
    private Usuario usuario;

    @Column(name = "descricao_alteracao", columnDefinition = "TEXT")
    private String descricaoAlteracao;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "dados_anteriores")
    private Map<String, Object> dadosAnteriores;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "dados_novos")
    private Map<String, Object> dadosNovos;

    @Generated(event = EventType.INSERT)
    @Column(name = "created_at", updatable = false, nullable = false)
    private OffsetDateTime createdAt;
}
