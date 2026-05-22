package com.fadergs.salalilas.backend.pdf.generator;

import com.fadergs.salalilas.backend.patient.dto.TimelineEventoResponse;
import com.fadergs.salalilas.backend.pdf.dto.PdfAtendimentoData;
import com.fadergs.salalilas.backend.triage.initial.dto.AnamneseInicialResponse;
import com.fadergs.salalilas.backend.triage.technical.dto.AnamneseTecnicaResponse;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Component
@Slf4j
public class AtendimentoPdfGenerator {
    private static final Font TITLE_FONT =
            new Font(Font.HELVETICA, 18, Font.BOLD, new Color(88, 28, 88));
    private static final Font SECTION_FONT =
            new Font(Font.HELVETICA, 13, Font.BOLD, new Color(88, 28, 88));
    private static final Font LABEL_FONT =
            new Font(Font.HELVETICA, 10, Font.BOLD);
    private static final Font VALUE_FONT =
            new Font(Font.HELVETICA, 10, Font.NORMAL);
    private static final Font SMALL_FONT =
            new Font(Font.HELVETICA, 8, Font.ITALIC, Color.GRAY);

    public byte[] generate(PdfAtendimentoData data) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4, 40, 40, 60, 40);
            PdfWriter.getInstance(document, baos);

            document.open();
            addContent(document, data);
            document.close();

            return baos.toByteArray();
        } catch (DocumentException e) {
            log.error("Error generating PDF for appointment", e);
            throw new RuntimeException("Erro ao gerar PDF do atendimento", e);
        }
    }

    private void addContent(Document doc, PdfAtendimentoData data) throws DocumentException {

        // -- Header --
        Paragraph title = new Paragraph("Sala Lilás - Relatório de Atendimento", TITLE_FONT);
        title.setAlignment(Element.ALIGN_CENTER);
        doc.add(title);

        Paragraph meta = new Paragraph(
                "Gerado em: " + data.geradoEm().format(
                        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                + "  |  Por: " + data.geradoPor(), SMALL_FONT);
        meta.setAlignment(Element.ALIGN_CENTER);
        doc.add(meta);
        doc.add(new Paragraph(" "));
        doc.add(new LineSeparator());
        doc.add(new Paragraph(" "));

        // -- Dados do Agendamento --
        addSection(doc, "Dados do Agendamento");
        addField(doc, "Paciente", data.pacienteNome());
        addField(doc, "CPF", formatCpf(data.pacienteCpf()));
        addField(doc, "Data", data.data().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        addField(doc, "Horário", data.horario().toString());
        if (data.atendenteNome() != null) {
            addField(doc, "Atendente", data.atendenteNome());
        }
        doc.add(new Paragraph(" "));

        // -- Anamnese Inicial --
        if (data.anamneseInicial() != null) {
            AnamneseInicialResponse a = data.anamneseInicial();
            addSection(doc, "Anamnese Inicial");
            addField(doc, "Tipo de Atendimento", a.tipoAtendimento());
            addField(doc, "Primeiro Atendimento",
                    a.primeiroAtendimento() ? "Sim" : "Não");
            if (a.territorio() != null)
                addField(doc, "Território/Localidade", a.territorio());
            addField(doc, "Cor/Raça", a.corRaca());
            addField(doc, "Sexo/Gênero", a.sexoGenero());
            if (a.sexoGeneroOutro() != null)
                addField(doc, "Sexo/Gênero (outro)", a.sexoGeneroOutro());
            if (a.violencias() != null && !a.violencias().isEmpty()) {
                String violencias = a.violencias().stream()
                        .map(v -> v.violenciaOutro() != null
                            ? v.violencia() + " (" + v.violenciaOutro() + ")"
                            : v.violencia())
                        .collect(Collectors.joining(", "));
                addField(doc, "Caracterização da Violência", violencias);
            }
            doc.add(new Paragraph(" "));
        }

        // -- Anamnese Técnica --
        if (data.anamneseTecnica() != null) {
            AnamneseTecnicaResponse t = data.anamneseTecnica();
            addSection(doc, "Anamnese Técnica");
            addField(doc, "Risco Iminente", t.riscoIminente() ? "Sim" : "Não");
            addField(doc, "Agressor Convive", t.agressorConvive() ? "Sim" : "Não");
            addField(doc, "Histórico de Violência", t.historicoViolencia() ? "Sim" : "Não");
            addField(doc, "Rede de Apio", t.redeApoio() ? "Sim" : "Não");
            addField(doc, "Filhos/Dependentes", t.filhosDependentes() ? "Sim" : "Não");
            if (t.observacoes() != null)
                addField(doc, "Observações", t.observacoes());
            if (t.registroAtendimento() != null)
                addField(doc, "Registro do Atendimento", t.registroAtendimento());
            if (t.orientacoes() != null && !t.orientacoes().isEmpty()) {
                String orientacoes = t.orientacoes().stream()
                        .map(o -> o.orientacaoOutro() != null
                            ? o.orientacao() + " (" + o.orientacaoOutro() + ")"
                            : o.orientacao())
                        .collect(Collectors.joining(", "));
                addField(doc, "Orientações Relizadas", orientacoes);
            }
            if (t.encaminhamentos() != null && !t.encaminhamentos().isEmpty()) {
                String emcaminhamentos = t.encaminhamentos().stream()
                        .map(o -> o.encaminhamentoOutro() != null
                                ? o.encaminhamento() + " (" + o.encaminhamentoOutro() + ")"
                                : o.encaminhamento())
                        .collect(Collectors.joining(", "));
                addField(doc, "Encaminhamentos Realizados", emcaminhamentos);
            }
            if (t.detalhamentoEncaminhamentos() != null)
                addField(doc, "Detalhamentos", t.detalhamentoEncaminhamentos());
            addField(doc, "Plano de Acompanhamento", t.planoAcompanhamento());
            if (t.dataRetorno() != null)
                addField(doc, "Data de Retorno",
                        t.dataRetorno().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            if (t.sinteseCaso() != null)
                addField(doc, "Síntese do Caso", t.sinteseCaso());
            if (t.objetivos() != null && !t.objetivos().isEmpty()) {
                String objetivos = t.objetivos().stream()
                        .map(o -> o.objetivo() != null
                                ? o.objetivo() + " (" + o.objetivoOutro() + ")"
                                : o.objetivo())
                        .collect(Collectors.joining(", "));
                addField(doc, "Objetivos do Encaminhamento", objetivos);
            }
            doc.add(new Paragraph(" "));
        }

        // -- Prontuário Psicossocial --
        if (data.prontuario() != null) {
            addSection(doc, "Prontuário Psicossocial");
            addField(doc, "Observações Psicossociais",
                    data.prontuario().observacoesPsicossociais());
            addField(doc, "Registrado por", data.prontuario().criadoPor());
            doc.add(new Paragraph(" "));
        }

        // -- Observação Jurídica
        if (data.obsJuridica() != null) {
            addSection(doc, "Observação Jurídica");
            addField(doc, "Encaminhamentos Legais",
                    data.obsJuridica().encaminhamentosLegais());
            addField(doc, "Registrado por", data.obsJuridica().criadoPor());
            doc.add(new Paragraph(" "));
        }

        // -- Timeline --
        if (data.timeline() != null && !data.timeline().isEmpty()) {
            addSection(doc, "Histórico do Atendimento");
            for (TimelineEventoResponse evento : data.timeline()) {
                String linha = evento.criadoEm().format(
                        DateTimeFormatter.ofPattern("dd/MM/yyyy HH::mm"))
                        + " - " + evento.evento()
                        + (evento.responsavel() != null
                            ? "  (" + evento.responsavel() + ")" : "");
                Paragraph p = new Paragraph(linha, VALUE_FONT);
                p.setIndentationLeft(10);
                doc.add(p);
                if (evento.descricao() != null) {
                    Paragraph desc = new Paragraph(
                            "    " + evento.descricao(), SMALL_FONT);
                    doc.add(desc);
                }
            }
        }
    }

    private void addSection(Document doc, String title) throws DocumentException {
        doc.add(new LineSeparator());
        Paragraph p = new Paragraph(title, SECTION_FONT);
        p.setSpacingBefore(8);
        p.setSpacingAfter(6);
        doc.add(p);
    }

    private void addField(Document doc, String label, String value) throws DocumentException {
        if (value == null || value.isBlank()) return;
        Phrase phrase = new Phrase();
        phrase.add(new Chunk(label + ": ", LABEL_FONT));
        phrase.add(new Chunk(value, VALUE_FONT));
        Paragraph p = new Paragraph(phrase);
        p.setIndentationLeft(10);
        p.setSpacingAfter(3);
        doc.add(p);
    }

    private String formatCpf(String cpf) {
        if (cpf == null || cpf.length() != 11) return cpf;
        return cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }
}
