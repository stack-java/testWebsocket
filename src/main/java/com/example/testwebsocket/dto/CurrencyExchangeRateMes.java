package com.example.testwebsocket.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyExchangeRateMes {

    private String currencyIn;
    private String currencyOut;
    private BigDecimal rate;

}
