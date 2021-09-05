package com.example.testwebsocket.dto.currencyExchange.partner;

import lombok.Data;

import java.util.List;

@Data
public class CurrateCurrencyListIn {

    private Integer status;
    private String message;
    private List<String> data;

}
