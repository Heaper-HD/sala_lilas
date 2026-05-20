package com.fadergs.salalilas.backend.appointment.service;

import com.fadergs.salalilas.backend.appointment.dto.AgendamentoPublicoRequest;
import com.fadergs.salalilas.backend.appointment.dto.AgendamentoPublicoReponse;
import com.fadergs.salalilas.backend.appointment.dto.HorariosDisponiveisResponse;
import com.fadergs.salalilas.backend.appointment.entity.Agendamento;
import com.fadergs.salalilas.backend.appointment.repository.AgendamentoRepository;
import com.fadergs.salalilas.backend.common.enums.StatusAtendimento;
import com.fadergs.salalilas.backend.exception.ErrorCode;
import com.fadergs.salalilas.backend.exception.types.BusinessException;
import com.fadergs.salalilas.backend.patient.entity.Paciente;
import com.fadergs.salalilas.backend.patient.repository.PacienteRepository;
import com.fadergs.salalilas.backend.util.CpfValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AgendamentoPublicoService {
    private static final LocalTime INICIO = LocalTime.of(9, 0);
    private static final LocalTime FIM = LocalTime.of(18, 0);
    private static final int INTERVALO_MINUTOS = 30;

    private final AgendamentoRepository agendamentoRepository;
    private final PacienteRepository pacienteRepository;

    public HorariosDisponiveisResponse listarHorariosDisponeis(LocalDate data) {
        validarDiaUtil(data);

        List<LocalTime> todos = gerarTodosHorarios();
        List<LocalTime> ocupados = agendamentoRepository.findHorariosOcupadosByData(data);

        List<LocalTime> disponiveis = todos.stream()
                .filter(h -> !ocupados.contains(h))
                .toList();

        return new HorariosDisponiveisResponse(disponiveis);
    }

    @Transactional
    public AgendamentoPublicoReponse criar(AgendamentoPublicoRequest request) {
        if (!CpfValidator.isValid(request.cpf())) {
            throw new BusinessException(ErrorCode.AGD_INVALID_CPF);
        }

        validarDiaUtil(request.data());

        validarHorario(request.horario());

        if (agendamentoRepository.existsByDataAndHorario(request.data(), request.horario())) {
            throw new BusinessException(ErrorCode.AGD_SLOT_UNAVAILABLE);
        }

        String cpfLimpo = CpfValidator.sanitize(request.cpf());
        Paciente paciente = pacienteRepository.findByCpf(cpfLimpo)
                .orElseGet(() -> pacienteRepository.save(
                        Paciente.builder()
                                .nome(request.nome())
                                .email(request.email())
                                .cpf(cpfLimpo)
                                .build()
                ));

        Agendamento agendamento = Agendamento.builder()
                .paciente(paciente)
                .data(request.data())
                .horario(request.horario())
                .status(StatusAtendimento.AGENDADO)
                .build();

        agendamento = agendamentoRepository.save(agendamento);
        log.info("Public appointment created for CPF {} on {} at {}",
                cpfLimpo, request.data(), request.horario());

        return new AgendamentoPublicoReponse(
                agendamento.getId(),
                agendamento.getStatus().name()
        );
    }

    private void validarDiaUtil(LocalDate data) {
        DayOfWeek dia = data.getDayOfWeek();
        if (dia == DayOfWeek.SATURDAY || dia == DayOfWeek.SUNDAY) {
            throw new BusinessException(ErrorCode.AGD_INVALID_DATE);
        }
    }

    private void validarHorario(LocalTime horario) {
        if (horario.isBefore(INICIO) || !horario.isBefore(FIM)) {
            throw new BusinessException(ErrorCode.AGD_INVALID_TIME);
        }
        int minutos = horario.getHour() * 60 + horario.getMinute();
        int inicio = INICIO.getHour() * 60;
        if ((minutos - inicio) % INTERVALO_MINUTOS != 0) {
            throw new BusinessException(ErrorCode.AGD_INVALID_TIME);
        }
    }

    private List<LocalTime> gerarTodosHorarios() {
        List<LocalTime> horarios = new ArrayList<>();
        LocalTime atual = INICIO;
        while (atual.isBefore(FIM)) {
            horarios.add(atual);
            atual = atual.plusMinutes(INTERVALO_MINUTOS);
        }
        return horarios;
    }
}
