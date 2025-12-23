package com.AIProd.demo.ingestion;

import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;
import java.util.stream.Stream;

@Component
public class FileBasedIngestionJob implements CommandLineRunner {

    private final KnowledgeIngestionService ingestionService;
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(FileBasedIngestionJob.class);

    public FileBasedIngestionJob(KnowledgeIngestionService ingestionService) {
        this.ingestionService = ingestionService;
    }

    @Override
    public void run(String... args) throws Exception {

        Path kbDir = Paths.get("kb");

        if (!Files.exists(kbDir)) {
            return;
        }

        try (Stream<Path> files = Files.list(kbDir)) {
            files.filter(Files::isRegularFile)
                    .forEach(file -> {
                        try {
                            String content = Files.readString(file).trim();

                            if (content.isBlank()) {
                                log.warn("Skipping empty file: {}", file.getFileName());
                                return;
                            }

                            ingestionService.ingestText(
                                    file.getFileName().toString(),
                                    content
                            );

                        } catch (Exception e) {
                            log.error("Failed to ingest file {}", file.getFileName(), e);
                        }
                    });
        }

    }
}
