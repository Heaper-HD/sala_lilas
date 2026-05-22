package com.fadergs.salalilas.backend.pdf.controller;

import com.fadergs.salalilas.backend.pdf.service.PdfService;
import com.fadergs.salalilas.backend.security.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/pdf")
@RequiredArgsConstructor
@Tag(name = "PDF", description = "Geração e download do documento consoldiade do atendimento")
public class PdfController {
    private final PdfService pdfService;

    @GetMapping("/{agendamentoId}")
    @PreAuthorize("hasAnyAuthority('TECNICA', 'CIS', 'NPJ', 'ADMIN')")
    @Operation(summary = "Gera ou baixa o PDF consolidade do atendimento")
    public ResponseEntity<byte[]> download(@PathVariable UUID agendamentoId) {
        UUID usuarioId = SecurityUtils.getCurrentUserId();
        byte[] pdf = pdfService.gerar(agendamentoId, usuarioId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment",
                "atendimento-" + agendamentoId + ".pdf");
        headers.setContentLength(pdf.length);

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdf);
    }
}
