package com.AIProd.demo.service;

import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class OrderNumberExtractor {
    private static final Pattern ORDER_PATTERN =
            Pattern.compile("(order\\s*#?\\s*)([A-Za-z0-9-]+)", Pattern.CASE_INSENSITIVE);
    public Optional<String> extract(String message) {
        Matcher matcher = ORDER_PATTERN.matcher(message);
        return matcher.find()
                ? Optional.of(matcher.group(2))
                : Optional.empty();
    }
}

