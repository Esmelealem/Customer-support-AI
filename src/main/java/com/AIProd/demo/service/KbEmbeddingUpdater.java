package com.AIProd.demo.service;

import com.AIProd.demo.utility.OpenAiEmbeddingClient;
import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class KbEmbeddingUpdater {

    private final JdbcTemplate jdbcTemplate;
    private final OpenAiEmbeddingClient embeddingClient;

    public KbEmbeddingUpdater(JdbcTemplate jdbcTemplate,
                              OpenAiEmbeddingClient embeddingClient) {
        this.jdbcTemplate = jdbcTemplate;
        this.embeddingClient = embeddingClient;
    }
    @Transactional
    public int generateAndStoreEmbeddings() {

        List<Map<String, Object>> rows =
                jdbcTemplate.queryForList(
                        "SELECT id, content FROM kb_docs WHERE embedding IS NULL"
                );

        for (Map<String, Object> row : rows) {
            Long id = ((Number) row.get("id")).longValue();
            String content = (String) row.get("content");

            float[] embedding = embeddingClient.embed(content);

            jdbcTemplate.update(
                    "UPDATE kb_docs SET embedding = CAST(? AS vector) WHERE id = ?",
                    embedding,
                    id
            );
        }

        return rows.size();
    }
}
