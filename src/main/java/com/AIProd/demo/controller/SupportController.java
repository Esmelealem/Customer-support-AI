package com.AIProd.demo.controller;

import com.AIProd.demo.service.SupportChatService;
import com.AIProd.demo.utility.prompt.SupportRequest;
import com.AIProd.demo.utility.prompt.SupportResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/support")
public class SupportController {
    private final SupportChatService chatService;
    public SupportController(SupportChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/chat")
    public SupportResponse chat(@RequestBody @Valid SupportRequest req) {
        return chatService.handle(req);
    }
}

