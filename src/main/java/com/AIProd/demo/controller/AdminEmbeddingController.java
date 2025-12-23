package com.AIProd.demo.controller;

import com.AIProd.demo.service.KbEmbeddingUpdater;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/embeddings")
public class AdminEmbeddingController {
    private final KbEmbeddingUpdater updater;
    public AdminEmbeddingController(KbEmbeddingUpdater updater) {
        this.updater = updater;
    }

    @PostMapping("/generate")
    public ResponseEntity<String> generateEmbeddings() {
        int count = updater.generateAndStoreEmbeddings();
        return ResponseEntity.ok("Generated embeddings for " + count + " records");
    }
}

