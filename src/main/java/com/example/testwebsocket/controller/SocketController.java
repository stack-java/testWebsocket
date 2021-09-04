package com.example.testwebsocket.controller;

import com.example.testwebsocket.service.CurrencyService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class SocketController {

    private CurrencyService currencyService;

    private SimpMessagingTemplate simpMessagingTemplate;

    public SocketController(CurrencyService currencyService, SimpMessagingTemplate simpMessagingTemplate) {
        this.currencyService = currencyService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public void send() {
        simpMessagingTemplate.convertAndSend(currencyService.getCurrencyExchangeRateRes());
    }

}
