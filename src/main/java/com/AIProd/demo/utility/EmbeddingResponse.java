package com.AIProd.demo.utility;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EmbeddingResponse(List<Data> data) {

    public float[] embedding() {
        return data.get(0).embedding();
    }

    public record Data(float[] embedding) {}
}
