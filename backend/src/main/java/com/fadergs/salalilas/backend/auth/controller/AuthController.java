package com.fadergs.salalilas.backend.auth.controller;

import com.fadergs.salalilas.backend.auth.dto.*;
import com.fadergs.salalilas.backend.auth.service.AuthService;
import com.fadergs.salalilas.backend.security.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Authentication and authorization endpoints")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Login with email and password")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh access token")
    public ResponseEntity<RefreshResponse> refresh(@Valid @RequestBody RefreshRequest request) {
        return ResponseEntity.ok(authService.refresh(request));
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout - invalid session")
    public ResponseEntity<Void> logout() {
        // TODO: Implement a blacklist for discarded tokens
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/lgpd/aceitar")
    @Operation(summary = "Accept LGPD terms")
    public ResponseEntity<LgpdResponse> aceitarLgpd() {
        UUID usuarioId = SecurityUtils.getCurrentUserId();
        authService.aceitarLgpd(usuarioId);
        return ResponseEntity.ok(authService.getLgpdStatus(usuarioId));
    }

    @GetMapping("/me")
    @Operation(summary = "Get currect logged in user")
    public ResponseEntity<MeResponse> me() {
        UUID usuarioId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(authService.getMe(usuarioId));
    }
}
