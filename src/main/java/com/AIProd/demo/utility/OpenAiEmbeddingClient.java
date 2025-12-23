package com.AIProd.demo.utility;

public interface OpenAiEmbeddingClient {
//    @CircuitBreaker(name = "openaiEmbedding")
    float[] embed(String text);
}
