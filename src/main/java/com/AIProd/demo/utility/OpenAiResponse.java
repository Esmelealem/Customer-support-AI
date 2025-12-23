package com.AIProd.demo.utility;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenAiResponse(List<Choice> choices) {

    public String firstMessage() {
        return choices.get(0).message().content();
    }

    public record Choice(Message message) {}
    public record Message(String role, String content) {}
}
