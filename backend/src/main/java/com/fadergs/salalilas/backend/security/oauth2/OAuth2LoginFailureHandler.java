package com.fadergs.salalilas.backend.security.oauth2;

import com.fadergs.salalilas.backend.config.AppProperties;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2LoginFailureHandler implements AuthenticationFailureHandler {
    private final AppProperties appProperties;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.warn("OAuth2 login failed: {}", exception.getMessage());

        String redirectUrl = UriComponentsBuilder
                .fromUriString(appProperties.getFrontend().getCallbackUrl()
                        .replace("/oauth2/callback", "/login"))
                .queryParam("error", exception.getMessage())
                .build()
                .toUriString();

        response.sendRedirect(redirectUrl);
    }
}
