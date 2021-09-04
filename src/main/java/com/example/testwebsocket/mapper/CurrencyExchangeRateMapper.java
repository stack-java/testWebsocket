package com.example.testwebsocket.mapper;

import com.example.testwebsocket.dto.CurrencyExchangeRateMes;
import com.example.testwebsocket.entity.CurrencyExchangeRateEntity;

import java.util.ArrayList;
import java.util.List;

public class CurrencyExchangeRateMapper {

    public static List<CurrencyExchangeRateMes> mapToListCurrencyExchangeRatesMes(List<CurrencyExchangeRateEntity> currencyExchangeRateEntityList){
        List<CurrencyExchangeRateMes> currencyExchangeRateMesList = new ArrayList<>();
        currencyExchangeRateEntityList.forEach(currencyExchangeRateEntity ->
                currencyExchangeRateMesList.add(mapToCurrencyExchangeRatesMes(currencyExchangeRateEntity)));
        return currencyExchangeRateMesList;
    }

    public static CurrencyExchangeRateMes mapToCurrencyExchangeRatesMes(CurrencyExchangeRateEntity currencyExchangeRateEntity){

        return CurrencyExchangeRateMes.builder()
                .currencyIn(currencyExchangeRateEntity.getCurrencyIn())
                .currencyOut(currencyExchangeRateEntity.getCurrencyOut())
                .rate(currencyExchangeRateEntity.getRate())
                .build();
    }

}
