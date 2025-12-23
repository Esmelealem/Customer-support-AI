package com.AIProd.demo.utility.cronjob;

import com.AIProd.demo.service.KbEmbeddingUpdater;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class KbEmbeddingBatchJob {

    private final KbEmbeddingUpdater updater;

    public KbEmbeddingBatchJob(KbEmbeddingUpdater updater) {
        this.updater = updater;
    }

    // Every night at 2 AM
    @Scheduled(cron = "0 0 2 * * *")
    public void runNightlyEmbeddingJob() {
        int count = updater.generateAndStoreEmbeddings();
        if (count > 0) {
            System.out.println("Generated embeddings for " + count + " KB records");
        }
    }
}

