package com.AIProd.demo.admin;

import com.AIProd.demo.ingestion.DatabaseIngestionJob;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/admin/kb")
public class AdminKnowledgeController {
    private final DatabaseIngestionJob batchJob;
    public AdminKnowledgeController(DatabaseIngestionJob batchJob) {
        this.batchJob = batchJob;
    }
    @PostMapping("/ingest")
    public Map<String, Object> ingest() {
        int count = batchJob.runBatch();
        return Map.of("processed", count);
    }
}

