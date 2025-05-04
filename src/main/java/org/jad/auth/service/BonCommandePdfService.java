package org.jad.auth.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BonCommandePdfService {

    private final TemplateEngine templateEngine;

    private Path tempDir;

    @PostConstruct
    public void init() throws IOException {
        tempDir = Files.createTempDirectory("bons-commande-pdf");
    }

    public File genererPdfCommande(Map<String, Object> dataModel, String fileName) {
        try {
            // 1. Générer le HTML à partir du template
            Context context = new Context();
            context.setVariables(dataModel);
            String htmlContent = templateEngine.process("bon-commande", context);

            // 2. Créer un fichier PDF temporaire
            File outputFile = tempDir.resolve(fileName + ".pdf").toFile();
            try (OutputStream os = new FileOutputStream(outputFile)) {
                PdfRendererBuilder builder = new PdfRendererBuilder();
                builder.useFastMode();
                builder.useDefaultPageSize(210, 200, PdfRendererBuilder.PageSizeUnits.MM);
                builder.withHtmlContent(htmlContent, getClass().getResource("/").toString());
                builder.toStream(os);
                builder.run();
            }

            return outputFile;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du bon de commande PDF", e);
        }
    }
}
