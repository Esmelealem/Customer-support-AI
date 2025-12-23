package com.AIProd.demo.service;

import com.AIProd.demo.utility.EmbeddingResponse;
import com.AIProd.demo.utility.OpenAiEmbeddingClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Map;

@Component
public class OpenAiEmbeddingClientImpl implements OpenAiEmbeddingClient {

    private static final String EMBEDDING_MODEL = "text-embedding-3-small";

    private final WebClient webClient;

    public OpenAiEmbeddingClientImpl(
            @Value("${openai.api-key}") String apiKey) {

        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1/embeddings")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Override
    public float[] embed(String text) {

        Map<String, Object> request = Map.of(
                "model", EMBEDDING_MODEL,
                "input", text
        );
        EmbeddingResponse response = webClient.post()
                .bodyValue(request)
                .retrieve()
                .bodyToMono(EmbeddingResponse.class)
                .timeout(Duration.ofSeconds(10))
                .retryWhen(Retry.fixedDelay(2, Duration.ofSeconds(1)))
                .block();

        return response.embedding();
    }
}

