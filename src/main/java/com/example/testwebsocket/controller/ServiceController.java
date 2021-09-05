package com.example.testwebsocket.controller;

import com.example.testwebsocket.dto.currencyExchange.client.CurrencyExchangeRateOutMes;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ServiceController {

    @SendTo("/topic/public")
    public CurrencyExchangeRateOutMes sendMessage() {
        return new CurrencyExchangeRateOutMes();
    }

}
