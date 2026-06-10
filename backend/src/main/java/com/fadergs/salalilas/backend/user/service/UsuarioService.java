package com.fadergs.salalilas.backend.user.service;

import com.fadergs.salalilas.backend.exception.ErrorCode;
import com.fadergs.salalilas.backend.exception.types.BusinessException;
import com.fadergs.salalilas.backend.exception.types.ResourceNotFoundException;
import com.fadergs.salalilas.backend.user.dto.*;
import com.fadergs.salalilas.backend.user.entity.Usuario;
import com.fadergs.salalilas.backend.user.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UsuarioSummaryResponse> listar(String busca) {
        List<Usuario> usuarios = busca != null && !busca.isBlank()
                ? usuarioRepository.findByNomeOrEmailContaining(busca)
                : usuarioRepository.findAllByOrderByNomeAsc();

        return usuarios.stream()
                .map(this::toSummary)
                .toList();
    }

    public UsuarioResponse buscarPorId(UUID id) {
        Usuario usuario = findOrThrow(id);
        return toResponse(usuario);
    }

    public UsuarioResponse criar(CreateUsuarioRequest request) {
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new BusinessException(ErrorCode.USR_EMAIL_ALREADY_EXISTS);
        }

        Usuario usuario = Usuario.builder()
                .nome(request.nome())
                .email(request.email())
                .senhaHash(passwordEncoder.encode(request.senha()))
                .perfil(request.perfil())
                .ativo(true)
                .lgpdAceito(false)
                .build();

        usuario = usuarioRepository.save(usuario);
        log.info("User created: {} with profile {}", usuario.getEmail(), usuario.getPerfil());

        return toResponse(usuario);
    }

    public UsuarioResponse atualizar(UUID id, UpdateUsuarioRequest request) {
        Usuario usuario = findOrThrow(id);

        if (!usuario.getEmail().equals(request.email())
                && usuarioRepository.existsByEmail(request.email())) {
            throw new BusinessException(ErrorCode.USR_EMAIL_ALREADY_EXISTS);
        }

        usuario.setNome(request.nome());
        usuario.setEmail(request.email());
        usuario.setPerfil(request.perfil());
        usuarioRepository.save(usuario);

        log.info("User updated: {}", usuario.getEmail());
        return toResponse(usuario);
    }

    public void alterarSenha(UUID targetId, UUID adminId, ResetarSenhaRequest request) {
        Usuario admin = findOrThrow(adminId);

        if (!passwordEncoder.matches(request.senhaAdmin(), admin.getSenhaHash())) {
            throw new BusinessException(ErrorCode.AUTH_INVALID_CREDENTIALS);
        }

        Usuario target = findOrThrow(targetId);
        target.setSenhaHash(passwordEncoder.encode(request.senhaUsuario()));
        usuarioRepository.save(target);

        log.info("Password reset for user {} by admin {}", target.getEmail(), admin.getEmail());
    }

    public void desativar(UUID id) {
        Usuario usuario = findOrThrow(id);

        if (!usuario.isAtivo()) {
            return;
        }

        usuario.setAtivo(false);
        usuarioRepository.save(usuario);
        log.info("User deactivated: {}", usuario.getEmail());
    }

    public void reativar(UUID id) {
        Usuario usuario = findOrThrow(id);

        if (usuario.isAtivo()) {
            return;
        }

        usuario.setAtivo(true);
        usuarioRepository.save(usuario);
        log.info("User reactivated: {}", usuario.getEmail());
    }

    private Usuario findOrThrow(UUID id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USR_NOT_FOUND));
    }

    private UsuarioResponse toResponse(Usuario u) {
        return new UsuarioResponse(
                u.getId(),
                u.getNome(),
                u.getEmail(),
                u.getPerfil().name(),
                u.isAtivo(),
                u.isLgpdAceito(),
                u.getLgpdData(),
                u.getCriadoEm()
        );
    }

    private UsuarioSummaryResponse toSummary(Usuario u) {
        return new UsuarioSummaryResponse(
                u.getId(),
                u.getNome(),
                u.getEmail(),
                u.getPerfil().name(),
                u.isAtivo(),
                u.isLgpdAceito(),
                u.getLgpdData()
        );
    }
}
