package com.example.testwebsocket.service;

import com.example.testwebsocket.dto.currencyExchange.client.CurrencyExchangeRateOut;
import com.example.testwebsocket.dto.currencyExchange.client.CurrencyExchangeRateOutMes;
import com.example.testwebsocket.dto.currencyExchange.client.TypeMesOut;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CurrencyService {

    private RestService restService;
    private SimpMessagingTemplate simpMessagingTemplate;

    public final ConcurrentHashMap<String, BigDecimal> currencyMapInMemory =
            new ConcurrentHashMap<>();

    public CurrencyService(RestService restService, SimpMessagingTemplate simpMessagingTemplate) {
        this.restService = restService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void getAndSaveCurrencyExchangeRate() {
        Map<String, BigDecimal> currencyExchangeInMap =
                restService.getCurrencyExchangeFromPartner();

        if (currencyExchangeInMap.size() != 0) {
            Map<String, BigDecimal> onlyNewCurrencyExchangeMap = new HashMap<>();
            Map<String, BigDecimal> onlyChangedCurrencyExchangeMap = new HashMap<>();

            for (Map.Entry<String, BigDecimal> entry : currencyExchangeInMap.entrySet()) {
                BigDecimal valueOld = currencyMapInMemory.get(entry.getKey());
                if (valueOld == null) {
                    onlyNewCurrencyExchangeMap.put(entry.getKey(), entry.getValue());
                    currencyMapInMemory.put(entry.getKey(), entry.getValue());
                } else {
                    if (!valueOld.equals(entry.getValue())) {
                        onlyChangedCurrencyExchangeMap.put(entry.getKey(), entry.getValue());
                        currencyMapInMemory.put(entry.getKey(), entry.getValue());
                    }
                }
            }

            CurrencyExchangeRateOutMes currencyExchangeRateOutMes = new CurrencyExchangeRateOutMes();
            currencyExchangeRateOutMes.setMessages(new ArrayList<>());

            if (onlyNewCurrencyExchangeMap.size() > 0){
                CurrencyExchangeRateOut currencyExchangeRateOut = new CurrencyExchangeRateOut();
                currencyExchangeRateOut.setCurrencyExchangeRates(onlyNewCurrencyExchangeMap);
                currencyExchangeRateOut.setType(TypeMesOut.NEW);

                currencyExchangeRateOutMes.getMessages().add(currencyExchangeRateOut);
            }
            if (onlyChangedCurrencyExchangeMap.size() > 0){
                CurrencyExchangeRateOut currencyExchangeRateOut = new CurrencyExchangeRateOut();
                currencyExchangeRateOut.setCurrencyExchangeRates(onlyChangedCurrencyExchangeMap);
                currencyExchangeRateOut.setType(TypeMesOut.CHANGE);

                currencyExchangeRateOutMes.getMessages().add(currencyExchangeRateOut);
            }

            if(currencyExchangeRateOutMes.getMessages().size() > 0){

                simpMessagingTemplate.convertAndSend("/topic/public", currencyExchangeRateOutMes);
            }

        }

    }

}
