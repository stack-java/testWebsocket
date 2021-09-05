package com.example.testwebsocket.dto.currencyExchange.client;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class CurrencyExchangeRateOut {

    private TypeMesOut type;
    private Map<String, BigDecimal> currencyExchangeRates;

}
