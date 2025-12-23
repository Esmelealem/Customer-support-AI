package com.AIProd.demo.ingestion;

import com.AIProd.demo.utility.OpenAiEmbeddingClient;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class KnowledgeIngestionService {
    private static final Logger log = LoggerFactory.getLogger(KnowledgeIngestionService.class);
    private static final int EXPECTED_EMBEDDING_DIM = 1536;

    private final JdbcTemplate jdbcTemplate;
    private final OpenAiEmbeddingClient embeddingClient;

    public KnowledgeIngestionService(JdbcTemplate jdbcTemplate,
                                     OpenAiEmbeddingClient embeddingClient) {
        this.jdbcTemplate = jdbcTemplate;
        this.embeddingClient = embeddingClient;
    }

    @Transactional
    public int ingestText(String title, String content) {
    // Validate inputs
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Content cannot be null or empty");
        }
//        float[] embedding = embeddingClient.embed(content);
//        String sql = """
//                INSERT INTO kb_docs (title, content, embedding, created_at, updated_at)
//                VALUES (?, ?, ?::vector, now(), now())
//                ON CONFLICT (lower(btrim(title)))
//                DO UPDATE SET
//                    content = EXCLUDED.content,
//                    embedding = EXCLUDED.embedding,
//                    updated_at = now()
//                """;
//        return jdbcTemplate.update(sql, title, content, embedding);
        try {
            // Generate embedding
            float[] embedding = embeddingClient.embed(content);
            log.debug("Generated embedding with {} dimensions for title: {}",
                    embedding.length, title);

            // Validate embedding dimensions
            if (embedding.length != EXPECTED_EMBEDDING_DIM) {
                log.warn("Unexpected embedding dimension: {} (expected: {})",
                        embedding.length, EXPECTED_EMBEDDING_DIM);
            }
            // Upsert into database
            jdbcTemplate.update(
                    """
                    INSERT INTO kb_docs (title, content, embedding)
                    VALUES (?, ?, CAST(? AS vector))
                    ON CONFLICT (lower(btrim(title)))
                    DO UPDATE SET
                        content    = EXCLUDED.content,
                        embedding  = EXCLUDED.embedding,
                        created_at = now()
                    """,
                    title,
                    content,
                    embedding
            );
            return 1;

        } catch (Exception e) {
            log.error("Failed to ingest document: {}", title, e);
            throw new RuntimeException("Failed to ingest document: " + title, e);
        }
    }
}

