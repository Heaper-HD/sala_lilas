package com.fadergs.salalilas.backend.security.oauth2;

import com.fadergs.salalilas.backend.config.AppProperties;
import com.fadergs.salalilas.backend.security.JwtService;
import com.fadergs.salalilas.backend.user.entity.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final AppProperties appProperties;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2UserPrincipal principal = (OAuth2UserPrincipal) authentication.getPrincipal();
        Usuario usuario = principal.getUsuario();

        log.info("OAuth2 login successful for user: {}", usuario.getEmail());

        String accessToken = jwtService.generateAccessToken(usuario);
        String refreshToken = jwtService.generateRefreshToken(usuario);

        boolean lgpdPendente = !usuario.isLgpdAceito();

        String redirectUrl = UriComponentsBuilder
                .fromUriString(appProperties.getFrontend().getCallbackUrl())
                .queryParam("access_token", accessToken)
                .queryParam("refresh_token", refreshToken)
                .queryParam("lgpd_pendente", lgpdPendente)
                .build()
                .toUriString();

        response.sendRedirect(redirectUrl);
    }
}
