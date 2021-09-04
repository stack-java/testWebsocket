package com.example.testwebsocket.dto;

import lombok.Data;

import java.util.List;

@Data
public class CurrencyExchangeRateRes {

    private List<CurrencyExchangeRateMes> currencyExchangeRates;

}
