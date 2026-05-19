package com.fadergs.salalilas.backend.appointment.repository;

import com.fadergs.salalilas.backend.appointment.entity.Agendamento;
import com.fadergs.salalilas.backend.common.annotation.RequiresDocker;
import com.fadergs.salalilas.backend.common.enums.StatusAtendimento;
import com.fadergs.salalilas.backend.patient.entity.Paciente;
import com.fadergs.salalilas.backend.patient.repository.PacienteRepository;
import com.fadergs.salalilas.backend.user.repository.UsuarioRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RequiresDocker
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class AgendamentoRepositoryTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("salalilas_test")
            .withUsername("postgres")
            .withPassword("postgres");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        if ("true".equals(System.getenv("DOCKER_AVAILABLE"))) {
            registry.add("spring.datasource.url", postgres::getJdbcUrl);
            registry.add("spring.datasource.username", postgres::getUsername);
            registry.add("spring.datasource.password", postgres::getPassword);
        }

        registry.add("spring.flyway.locations", () -> "filesystem:../db/migration");
    }

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Paciente paciente;
    private LocalDate hoje;

    @BeforeEach
    void setUp() {
        hoje = LocalDate.now();

        paciente = pacienteRepository.save(Paciente.builder()
                .nome("Maria Silva")
                .email("maria@email.com")
                .cpf("52998224725")
                .build());
    }

    @Test
    @DisplayName("Should detect slot already taken (RN007)")
    void shouldDetectSlotAlreadyTaken() {
        agendamentoRepository.save(Agendamento.builder()
                .paciente(paciente)
                .data(hoje)
                .horario(LocalTime.of(9, 0))
                .status(StatusAtendimento.AGENDADO)
                .build());

        assertThat(agendamentoRepository
                .existsByDataAndHorario(hoje, LocalTime.of(9, 0)))
                .isTrue();

        assertThat(agendamentoRepository
                .existsByDataAndHorario(hoje, LocalTime.of(9, 30)))
                .isFalse();
    }

    @Test
    @DisplayName("Should return occupied horarios for a date")
    void shouldReturnOccupiedHorarios() {
        agendamentoRepository.save(Agendamento.builder()
                .paciente(paciente)
                .data(hoje)
                .horario(LocalTime.of(9, 0))
                .status(StatusAtendimento.AGENDADO)
                .build());

        agendamentoRepository.save(Agendamento.builder()
                .paciente(paciente)
                .data(hoje)
                .horario(LocalTime.of(10, 0))
                .status(StatusAtendimento.AGENDADO)
                .build());

        List<LocalTime> ocupados = agendamentoRepository
                .findHorariosOcupadosByData(hoje);

        assertThat(ocupados).hasSize(2)
                .contains(LocalTime.of(9, 0), LocalTime.of(10, 0));
    }

    @Test
    @DisplayName("Should count correctly by status for dashboard counters")
    void shouldCountByStatusForDashboard() {
        agendamentoRepository.save(Agendamento.builder()
                .paciente(paciente)
                .data(hoje)
                .horario(LocalTime.of(9, 0))
                .status(StatusAtendimento.AGENDADO)
                .build());

        agendamentoRepository.save(Agendamento.builder()
                .paciente(paciente)
                .data(hoje)
                .horario(LocalTime.of(9, 30))
                .status(StatusAtendimento.TRIAGEM)
                .build());

        assertThat(agendamentoRepository
                .countByDataAndStatus(hoje, StatusAtendimento.AGENDADO))
                .isEqualTo(1);

        assertThat(agendamentoRepository
                .countByDataAndStatus(hoje, StatusAtendimento.TRIAGEM))
                .isEqualTo(1);
    }

    @Test
    @DisplayName("Should filter queue by status and date")
    void shouldFilterQueueByStatusAndDate() {
        agendamentoRepository.save(Agendamento.builder()
                .paciente(paciente)
                .data(hoje)
                .horario(LocalTime.of(9, 0))
                .status(StatusAtendimento.TECNICA)
                .build());

        agendamentoRepository.save(Agendamento.builder()
                .paciente(paciente)
                .data(hoje)
                .horario(LocalTime.of(10, 0))
                .status(StatusAtendimento.PSICOLOGIA)
                .build());

        List<Agendamento> filaTecnica = agendamentoRepository
                .findByStatusAndDataOrderByHorarioAsc(StatusAtendimento.TECNICA, hoje);

        assertThat(filaTecnica).hasSize(1);
        assertThat(filaTecnica.get(0).getStatus())
                .isEqualTo(StatusAtendimento.TECNICA);
    }

    @AfterEach
    void tearDown() {
        agendamentoRepository.deleteAll();
        pacienteRepository.deleteAll();
    }
}