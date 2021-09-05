package com.example.testwebsocket.dto.currencyExchange.partner;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class CurrateCurrencyExchangeIn {

    private Integer status;
    private String message;
    private Map<String, BigDecimal> data;

}
