package com.fadergs.salalilas.backend.auth.service;

import com.fadergs.salalilas.backend.auth.dto.*;
import com.fadergs.salalilas.backend.exception.ErrorCode;
import com.fadergs.salalilas.backend.exception.types.AuthException;
import com.fadergs.salalilas.backend.exception.types.ResourceNotFoundException;
import com.fadergs.salalilas.backend.security.JwtService;
import com.fadergs.salalilas.backend.user.entity.Usuario;
import com.fadergs.salalilas.backend.user.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.senha()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new AuthException(ErrorCode.AUTH_INVALID_CREDENTIALS);
        }

        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new AuthException(ErrorCode.AUTH_INVALID_CREDENTIALS));

        if (!usuario.isAtivo()) {
            throw new AuthException(ErrorCode.AUTH_USER_DISABLED);
        }

        String accessToken = jwtService.generateAccessToken(usuario);
        String refreshToken = jwtService.generateRefreshToken(usuario);

        log.info("User {} logged in successfully", usuario.getEmail());

        return new LoginResponse(
                accessToken,
                refreshToken,
                usuario.getPerfil().name(),
                !usuario.isLgpdAceito()
        );
    }

    public RefreshResponse refresh(RefreshRequest request) {
        String token = request.refreshToken();

        if (!jwtService.isTokenValid(token) || !jwtService.isRefreshToken(token)) {
            throw new AuthException(ErrorCode.AUTH_TOKEN_INVALID);
        }

        String email = jwtService.extractEmail(token);
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new AuthException(ErrorCode.AUTH_TOKEN_INVALID));

        if (!usuario.isAtivo()) {
            throw new AuthException(ErrorCode.AUTH_USER_DISABLED);
        }

        return new RefreshResponse(jwtService.generateAccessToken(usuario));
    }

    public void aceitarLgpd(UUID usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USR_NOT_FOUND));

        if (usuario.isLgpdAceito()) {
            return;
        }

        usuario.setLgpdAceito(true);
        usuario.setLgpdData(OffsetDateTime.now());
        usuarioRepository.save(usuario);

        log.info("User {} accepted LGPD terms at {}", usuario.getEmail(), usuario.getLgpdData());
    }

    public LgpdResponse getLgpdStatus(UUID usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USR_NOT_FOUND));

        return new LgpdResponse(
                usuario.isLgpdAceito(),
                usuario.getLgpdData()
        );
    }

    public MeResponse getMe(UUID usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USR_NOT_FOUND));

        return new MeResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getPerfil().name(),
                usuario.isLgpdAceito(),
                usuario.getLgpdData()
        );
    }
}
