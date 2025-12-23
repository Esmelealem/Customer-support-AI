package com.AIProd.demo.utility;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IntentRouter {

    private static final List<String> ORDER_KEYWORDS = List.of(
            "order", "tracking", "track", "shipment", "shipped", "#"
    );

    public Intent route(String message) {
        String lower = message.toLowerCase();

        if (containsAny(lower, ORDER_KEYWORDS)) {
            return Intent.ORDER_STATUS;
        }
        return Intent.POLICY_QUESTION;
    }

    private boolean containsAny(String text, List<String> keywords) {
        return keywords.stream().anyMatch(text::contains);
    }
}
