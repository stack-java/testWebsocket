package com.example.testwebsocket.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class Scheduler {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final CurrencyService currencyService;

    public Scheduler(SimpMessagingTemplate simpMessagingTemplate, CurrencyService currencyService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.currencyService = currencyService;
    }

    @Scheduled(fixedRate = 10000)
    public void getCurrencyExchangeRate() {
        currencyService.getAndSaveCurrencyExchangeRate();
    }

}
