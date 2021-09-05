package com.example.testwebsocket.dto.currencyExchange.client;

import lombok.Data;

import java.util.List;

@Data
public class CurrencyExchangeRateOutMes {

    private List<CurrencyExchangeRateOut> messages;

}
