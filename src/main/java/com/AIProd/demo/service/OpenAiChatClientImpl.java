package com.AIProd.demo.service;

import com.AIProd.demo.utility.OpenAiChatClient;
import com.AIProd.demo.utility.OpenAiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Component
public class OpenAiChatClientImpl implements OpenAiChatClient {

    private final WebClient webClient;

    public OpenAiChatClientImpl(
            @Value("${openai.api-key}") String apiKey) {

        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1/chat/completions")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
    @Override
    public String chat(String prompt) {
        Map<String, Object> body = Map.of(
                "model", "gpt-4o-mini",
                "messages", List.of(
                        Map.of("role", "system", "content",
                                "You are a customer support assistant. Use only provided context."),
                        Map.of("role", "user", "content", prompt)
                ),
                "temperature", 0.2
        );
        return webClient.post()
                .bodyValue(body)
                .retrieve()
                .bodyToMono(OpenAiResponse.class)
                .timeout(Duration.ofSeconds(10))
                .retryWhen(Retry.fixedDelay(2, Duration.ofSeconds(1)))
                .map(r -> r.firstMessage())
                .block();
    }
}

