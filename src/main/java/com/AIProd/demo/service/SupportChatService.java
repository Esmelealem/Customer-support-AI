package com.AIProd.demo.service;

import com.AIProd.demo.dto.KnowledgeSnippet;
import com.AIProd.demo.repository.OrderRepository;
import com.AIProd.demo.utility.Intent;
import com.AIProd.demo.utility.IntentRouter;
import com.AIProd.demo.utility.OpenAiChatClient;
import com.AIProd.demo.utility.prompt.PromptBuilder;
import com.AIProd.demo.utility.prompt.SupportRequest;
import com.AIProd.demo.utility.prompt.SupportResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class SupportChatService {

    private final IntentRouter intentRouter;
    private final OrderRepository orderRepo;
    private final KnowledgeBaseService kb;
    private final OpenAiChatClient openAi;
    private final OrderNumberExtractor orderNumberExtractor;

    public SupportChatService(IntentRouter intentRouter,
                              OrderRepository orderRepo,
                              KnowledgeBaseService kb,
                              OpenAiChatClient openAi,
                              OrderNumberExtractor orderNumberExtractor) {

        this.intentRouter = intentRouter;
        this.orderRepo = orderRepo;
        this.kb = kb;
        this.openAi = openAi;
        this.orderNumberExtractor = orderNumberExtractor;
    }

    public SupportResponse handle(SupportRequest req) {

        Intent intent = intentRouter.route(req.message());

        String orderContext = "";

        // ---------- ORDER FLOW ----------
        if (intent == Intent.ORDER_STATUS) {

            Optional<String> orderNumberOpt =
                    orderNumberExtractor.extract(req.message());

            if (orderNumberOpt.isEmpty()) {
                return SupportResponse.of(
                        "Can you please provide your order number so I can check the status?",
                        List.of("UserInput")
                );
            }

            orderContext = orderRepo
                    .findByOrderNumberAndUserId(orderNumberOpt.get(), req.userId())
                    .map(o -> "Order Number: %s, Status: %s"
                            .formatted(o.getOrderNumber(), o.getStatus()))
                    .orElse("No matching order found for the provided order number.");
        }

        // ---------- KNOWLEDGE BASE (RAG) ----------
        var kbSnippets = kb.search(req.message(), 5);

        // ---------- PROMPT ----------
        String prompt = PromptBuilder.build(
                req.message(),
                orderContext,
                kbSnippets
        );

        // ---------- LLM CALL ----------
        String answer = openAi.chat(prompt);

        // ---------- RESPONSE ----------
        return SupportResponse.of(
                answer,
                buildSources(intent, kbSnippets)
        );
    }

    private List<String> buildSources(Intent intent, List<KnowledgeSnippet> kbSnippets) {

        List<String> sources = new ArrayList<>();

        if (intent == Intent.ORDER_STATUS) {
            sources.add("OrderDB");
        }

        if (!kbSnippets.isEmpty()) {
            sources.add("KnowledgeBase");
        }

        return sources;
    }
}
