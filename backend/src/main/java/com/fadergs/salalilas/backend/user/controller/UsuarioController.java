package com.fadergs.salalilas.backend.user.controller;

import com.fadergs.salalilas.backend.user.dto.CreateUsuarioRequest;
import com.fadergs.salalilas.backend.user.dto.UpdateUsuarioRequest;
import com.fadergs.salalilas.backend.user.dto.UsuarioResponse;
import com.fadergs.salalilas.backend.user.dto.UsuarioSummaryResponse;
import com.fadergs.salalilas.backend.user.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "Gestão de usuários internos - acesso exclusivo do Administrador")
public class UsuarioController {
    private final UsuarioService usuarioService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Lista de todos os colaboradores")
    public ResponseEntity<List<UsuarioSummaryResponse>> listar(
            @RequestParam(required = false) String busca) {
        return ResponseEntity.ok(usuarioService.listar(busca));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Retorna dados completos de um usuário")
    public ResponseEntity<UsuarioResponse> buscar(@PathVariable UUID id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Cria novo usuário interno")
    public ResponseEntity<UsuarioResponse> criar(
            @Valid @RequestBody CreateUsuarioRequest request) {
        return ResponseEntity.ok(usuarioService.criar(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Atualiza dados e perfil de um usuário")
    public ResponseEntity<UsuarioResponse> atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateUsuarioRequest request) {
        return ResponseEntity.ok(usuarioService.atualizar(id, request));
    }

    @PatchMapping("/{id}/desativar")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Desativa o acesso do usuário (soft delete)")
    public ResponseEntity<Void> desativar(@PathVariable UUID id) {
        usuarioService.desativar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reativar")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Reativa um usuário previamente desativado")
    public ResponseEntity<Void> reativar(@PathVariable UUID id) {
        usuarioService.reativar(id);
        return ResponseEntity.noContent().build();
    }
}
