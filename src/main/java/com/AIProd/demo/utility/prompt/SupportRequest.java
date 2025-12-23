package com.AIProd.demo.utility.prompt;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SupportRequest(
        @NotNull(message = "userId is required")
        Long userId,
        @NotBlank(message = "message cannot be empty")
        @Size(max = 2000, message = "message too long")
        String message
) {
}

