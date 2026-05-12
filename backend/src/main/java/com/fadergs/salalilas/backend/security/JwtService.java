package com.fadergs.salalilas.backend.security;

import com.fadergs.salalilas.backend.config.AppProperties;
import com.fadergs.salalilas.backend.user.entity.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {
    private final AppProperties appProperties;

    private static final String CLAIM_PERFIL = "perfil";
    private static final String CLAIM_USER_ID = "userId";
    private static final String CLAIM_TOKEN_TYPE = "type";
    private static final String TYPE_ACCESS = "access";
    private static final String TYPE_REFRESH = "refresh";

    public String generateAccessToken(Usuario usuario) {
        return buildToken(usuario, TYPE_ACCESS,
                appProperties.getJwt().getAccessTokenExpiration());
    }

    public String generateRefreshToken(Usuario usuario) {
        return buildToken(usuario, TYPE_REFRESH,
                appProperties.getJwt().getRefreshTokenExpiration());
    }

    private String buildToken(Usuario usuario, String type, long expiration) {
        return Jwts.builder()
                .subject(usuario.getEmail())
                .claim(CLAIM_USER_ID, usuario.getId().toString())
                .claim(CLAIM_PERFIL, usuario.getPerfil().name())
                .claim(CLAIM_TOKEN_TYPE, type)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    public boolean isAccessToken(String token) {
        return TYPE_ACCESS.equals(getClaim(token, c -> c.get(CLAIM_TOKEN_TYPE, String.class)));
    }

    public boolean isRefreshToken(String token) {
        return TYPE_REFRESH.equals(getClaim(token, c -> c.get(CLAIM_TOKEN_TYPE, String.class)));
    }

    public String extractEmail(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public UUID extractUserId(String token) {
        return UUID.fromString(
                getClaim(token, c -> c.get(CLAIM_USER_ID, String.class))
        );
    }

    public boolean isTokenValid(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.debug("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    private <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(parseClaims(token));
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(
                appProperties.getJwt().getSecret().getBytes(StandardCharsets.UTF_8)
        );
    }
}
