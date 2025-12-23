package com.AIProd.demo.service;

import com.AIProd.demo.dto.KnowledgeSnippet;
import com.AIProd.demo.utility.OpenAiEmbeddingClient;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KnowledgeBaseService {
    private final JdbcTemplate jdbcTemplate;
    private final OpenAiEmbeddingClient embeddingClient;

    public KnowledgeBaseService(JdbcTemplate jdbcTemplate,
                                OpenAiEmbeddingClient embeddingClient) {
        this.jdbcTemplate = jdbcTemplate;
        this.embeddingClient = embeddingClient;
    }
    public List<KnowledgeSnippet> search(String query, int limit) {
        float[] embedding = embeddingClient.embed(query);
        String sql = """
            SELECT title, content
            FROM kb_docs
            ORDER BY embedding <-> CAST(? AS vector)
            LIMIT ?
        """;
        return jdbcTemplate.query(
                sql,
                ps -> {
                    ps.setObject(1, embedding);
                    ps.setInt(2, limit);
                },
                (rs, rowNum) -> new KnowledgeSnippet(
                        rs.getString("title"),
                        rs.getString("content")
                )
        );
    }
}

