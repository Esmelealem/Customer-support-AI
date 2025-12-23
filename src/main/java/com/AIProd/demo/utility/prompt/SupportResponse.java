package com.AIProd.demo.utility.prompt;

import java.time.Instant;
import java.util.List;

public record SupportResponse(String answer,

                              List<String> sources,

                              Instant timestamp
) {
    public static SupportResponse of(String answer, List<String> sources) {
        return new SupportResponse(answer, sources, Instant.now());
    }
}

