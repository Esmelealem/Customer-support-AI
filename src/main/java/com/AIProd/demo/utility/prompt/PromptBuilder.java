package com.AIProd.demo.utility.prompt;

import com.AIProd.demo.dto.KnowledgeSnippet;

import java.util.List;
import java.util.stream.Collectors;

public class PromptBuilder {

    public static String build(String userMessage,
                               String orderContext,
                               List<KnowledgeSnippet> kb) {

        String kbText = kb.stream()
                .map(k -> "- " + k.content())
                .collect(Collectors.joining("\n"));

        return """
            USER QUESTION:
            %s

            ORDER DATA:
            %s

            KNOWLEDGE BASE:
            %s

            Answer clearly and truthfully.
            """.formatted(userMessage, orderContext, kbText);
    }
}
