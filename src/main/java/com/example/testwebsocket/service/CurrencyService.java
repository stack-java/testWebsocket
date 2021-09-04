package com.example.testwebsocket.service;

import com.example.testwebsocket.dto.CurrencyExchangeRateRes;
import com.example.testwebsocket.entity.CurrencyExchangeRateEntity;
import com.example.testwebsocket.mapper.CurrencyExchangeRateMapper;
import com.example.testwebsocket.repository.CurrencyExchangeRateRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CurrencyService {

    private CurrencyExchangeRateRepository currencyExchangeRateRepository;

    public CurrencyService(CurrencyExchangeRateRepository currencyExchangeRateRepository) {
        this.currencyExchangeRateRepository = currencyExchangeRateRepository;
    }

    public CurrencyExchangeRateRes getCurrencyExchangeRateRes() {
        List<CurrencyExchangeRateEntity> currencyExchangeRateEntityList =
                StreamSupport
                        .stream(currencyExchangeRateRepository.findAll().spliterator(), false)
                        .collect(Collectors.toList());

        CurrencyExchangeRateRes currencyExchangeRateRes = new CurrencyExchangeRateRes();
        currencyExchangeRateRes.setCurrencyExchangeRates(
                CurrencyExchangeRateMapper.mapToListCurrencyExchangeRatesMes(currencyExchangeRateEntityList));

        return currencyExchangeRateRes;
    }
}
