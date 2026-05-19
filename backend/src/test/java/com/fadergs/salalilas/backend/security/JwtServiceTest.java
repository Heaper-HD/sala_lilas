package com.fadergs.salalilas.backend.security;

import com.fadergs.salalilas.backend.common.UnitTest;
import com.fadergs.salalilas.backend.common.enums.PerfilUsuario;
import com.fadergs.salalilas.backend.config.AppProperties;
import com.fadergs.salalilas.backend.user.entity.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class JwtServiceTest extends UnitTest {
    private JwtService jwtService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        AppProperties appProperties = new AppProperties();
        AppProperties.Jwt jwtProps = new AppProperties.Jwt();

        jwtProps.setSecret("test-secret-key-that-is-long-enough-for-hmac-sha256-algorithm");
        jwtProps.setAccessTokenExpiration(3600000L);
        jwtProps.setRefreshTokenExpiration(604800000L);
        appProperties.setJwt(jwtProps);

        jwtService = new JwtService(appProperties);

        usuario = Usuario.builder()
                .id(UUID.randomUUID())
                .email("atendente@salalilas.com")
                .perfil(PerfilUsuario.ATENDENTE)
                .ativo(true)
                .build();
    }

    @Test
    @DisplayName("Should generate a valid access token")
    void shouldGenerateValidAccessToken() {
        String token = jwtService.generateAccessToken(usuario);

        assertThat(token).isNotNull().isNotBlank();
        assertThat(jwtService.isTokenValid(token)).isTrue();
        assertThat(jwtService.isAccessToken(token)).isTrue();
    }

    @Test
    @DisplayName("Should generate a valid refresh token")
    void shouldGenerateValidRefreshToken() {
        String token = jwtService.generateRefreshToken(usuario);

        assertThat(token).isNotNull().isNotBlank();
        assertThat(jwtService.isTokenValid(token)).isTrue();
        assertThat(jwtService.isRefreshToken(token)).isTrue();
    }

    @Test
    @DisplayName("Access token should not be accepted as refresh token")
    void accessTokenShouldNotBeRefreshToken() {
        String token = jwtService.generateAccessToken(usuario);

        assertThat(jwtService.isRefreshToken(token)).isFalse();
    }

    @Test
    @DisplayName("Should extract correct email from token")
    void shouldExtractEmailFromToken() {
        String token = jwtService.generateAccessToken(usuario);

        assertThat(jwtService.extractEmail(token))
                .isEqualTo("atendente@salalilas.com");
    }

    @Test
    @DisplayName("Should extract correct userId from token")
    void shouldExtractUserIdFromToken() {
        String token = jwtService.generateAccessToken(usuario);

        assertThat(jwtService.extractUserId(token))
                .isEqualTo(usuario.getId());
    }

    @Test
    @DisplayName("Should extract correct perfil from token")
    void shouldExtractPerfilFromToken() {
        String token = jwtService.generateAccessToken(usuario);

        assertThat(jwtService.extractPerfil(token))
                .isEqualTo(PerfilUsuario.ATENDENTE);
    }

    @Test
    @DisplayName("Should reject a tampered token")
    void shouldRejectTamperedToken() {
        String token = jwtService.generateAccessToken(usuario);
        String tampered = token.substring(0, token.length() - 5) + "XXXXX";

        assertThat(jwtService.isTokenValid(tampered)).isFalse();
    }

    @Test
    @DisplayName("Should reject an expired token")
    void shouldRejectExpiredToken() {
        // Create a separate JwtService instance with -1ms expiration
        AppProperties expiredProps = new AppProperties();
        AppProperties.Jwt expiredJwt = new AppProperties.Jwt();
        expiredJwt.setSecret("test-secret-key-that-is-long-enough-for-hmac-sha256-algorithm");
        expiredJwt.setAccessTokenExpiration(-1L);  // already expired
        expiredJwt.setRefreshTokenExpiration(-1L);
        expiredProps.setJwt(expiredJwt);

        JwtService expiredJwtService = new JwtService(expiredProps);
        String token = expiredJwtService.generateAccessToken(usuario);

        assertThat(jwtService.isTokenValid(token)).isFalse();
    }

    @Test
    @DisplayName("Should reject a blank token")
    void shouldRejectBlankToken() {
        assertThat(jwtService.isTokenValid("")).isFalse();
        assertThat(jwtService.isTokenValid("not.a.jwt")).isFalse();
    }
}
