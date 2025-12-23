package com.AIProd.demo.ingestion;

import com.AIProd.demo.utility.OpenAiEmbeddingClient;
import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class DatabaseIngestionJob {
    private final JdbcTemplate jdbcTemplate;
    private final OpenAiEmbeddingClient embeddingClient;

    public DatabaseIngestionJob(JdbcTemplate jdbcTemplate,
                                OpenAiEmbeddingClient embeddingClient) {
        this.jdbcTemplate = jdbcTemplate;
        this.embeddingClient = embeddingClient;
    }
    @Transactional
    public int runBatch() {

        List<Map<String, Object>> rows =
                jdbcTemplate.queryForList(
                        "SELECT id, content FROM kb_docs WHERE embedding IS NULL"
                );

        for (var row : rows) {
            float[] embedding = embeddingClient.embed(
                    (String) row.get("content")
            );

            jdbcTemplate.update(
                    "UPDATE kb_docs SET embedding = CAST(? AS vector) WHERE id = ?",
                    embedding,
                    row.get("id")
            );
        }
        return rows.size();
    }
}

