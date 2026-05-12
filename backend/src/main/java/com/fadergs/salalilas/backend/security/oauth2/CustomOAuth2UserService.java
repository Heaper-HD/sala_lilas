package com.fadergs.salalilas.backend.security.oauth2;

import com.fadergs.salalilas.backend.exception.ErrorCode;
import com.fadergs.salalilas.backend.user.entity.Usuario;
import com.fadergs.salalilas.backend.user.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UsuarioRepository usuarioRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = extractEmail(oAuth2User, userRequest);

        log.debug("OAuth2 login attempt for email: {}", email);

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new OAuth2AuthenticationException(
                        new OAuth2Error("user_not_found"),
                        ErrorCode.AUTH_OAUTH2_ACCOUNT_NOT_FOUND.getDefaultMessage()
                ));

        if (!usuario.isAtivo()) {
            throw new OAuth2AuthenticationException(
                    new OAuth2Error("user_disabled"),
                    ErrorCode.AUTH_USER_DISABLED.getDefaultMessage()
            );
        }

        return new OAuth2UserPrincipal(oAuth2User, usuario);
    }

    private String extractEmail(OAuth2User oAuth2User, OAuth2UserRequest userRequest) {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String email = switch (registrationId) {
            case "google" -> oAuth2User.getAttribute("email");
            case "microsoft" -> oAuth2User.getAttribute("email");
            default -> oAuth2User.getAttribute("email");
        };

        if (email == null || email.isBlank()) {
            throw new OAuth2AuthenticationException(
                    new OAuth2Error("email_not_found"),
                    "Email não encontrado no provedor OAuth2"
            );
        }

        return email;
    }
}
