package com.example.testwebsocket.dto.currencyExchange.partner;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Map;

@Data
public class ApilayerCurrencyExchangeIn {

    private Boolean success;
    private String terms;
    private String privacy;
    private Timestamp timestamp;
    private String source;
    private Map<String, BigDecimal> quotes;

}

