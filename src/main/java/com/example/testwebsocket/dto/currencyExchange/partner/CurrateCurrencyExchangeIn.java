package com.example.testwebsocket.dto.currencyExchange.partner;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class CurrateCurrencyExchangeIn extends CurrencyExchangeFromPartner {

    private Integer status;
    private String message;
    private Map<String, BigDecimal> data;

    @Override
    public Map<String, BigDecimal> getCurrencyExchangeFromPartner() {
        return data;
    }
}
