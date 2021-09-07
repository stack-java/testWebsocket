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
import java.util.stream.Collectors;

@Service
public class CurrencyService {

    private PartnerService partnerService;
    private SimpMessagingTemplate simpMessagingTemplate;

    public final ConcurrentHashMap<String, BigDecimal> currencyMapInMemory =
            new ConcurrentHashMap<>();

    public CurrencyService(PartnerService partnerService, SimpMessagingTemplate simpMessagingTemplate) {
        this.partnerService = partnerService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void getAndSaveCurrencyExchangeRate() {
        Map<String, BigDecimal> gotByPartnerCurrencyExchangeMap =
                partnerService.getCurrencyExchangeFromPartner();

        if (!gotByPartnerCurrencyExchangeMap.isEmpty()) {
            Map<String, BigDecimal> onlyNewCurrencyExchangeMap = new HashMap<>();
            Map<String, BigDecimal> onlyChangedCurrencyExchangeMap = new HashMap<>();

            onlyNewCurrencyExchangeMap = gotByPartnerCurrencyExchangeMap.entrySet().stream()
                    .filter(entry -> currencyMapInMemory.get(entry.getKey()) == null)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            onlyChangedCurrencyExchangeMap = gotByPartnerCurrencyExchangeMap.entrySet().stream()
                    .filter(entry -> currencyMapInMemory.get(entry.getKey()) != null
                            && !currencyMapInMemory.get(entry.getKey()).equals(entry.getValue()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            CurrencyExchangeRateOutMes currencyExchangeRateOutMes = new CurrencyExchangeRateOutMes();
            currencyExchangeRateOutMes.setMessages(new ArrayList<>());

            if (!onlyNewCurrencyExchangeMap.isEmpty()) {

                currencyMapInMemory.putAll(onlyNewCurrencyExchangeMap);

                CurrencyExchangeRateOut currencyExchangeRateOut = new CurrencyExchangeRateOut();
                currencyExchangeRateOut.setCurrencyExchangeRates(onlyNewCurrencyExchangeMap);
                currencyExchangeRateOut.setType(TypeMesOut.NEW);

                currencyExchangeRateOutMes.getMessages().add(currencyExchangeRateOut);
            }
            if (!onlyChangedCurrencyExchangeMap.isEmpty()) {

                currencyMapInMemory.putAll(onlyChangedCurrencyExchangeMap);

                CurrencyExchangeRateOut currencyExchangeRateOut = new CurrencyExchangeRateOut();
                currencyExchangeRateOut.setCurrencyExchangeRates(onlyChangedCurrencyExchangeMap);
                currencyExchangeRateOut.setType(TypeMesOut.CHANGE);

                currencyExchangeRateOutMes.getMessages().add(currencyExchangeRateOut);
            }

            if (!currencyExchangeRateOutMes.getMessages().isEmpty()) {
                simpMessagingTemplate.convertAndSend("/topic/public", currencyExchangeRateOutMes);
            }

        }

    }

}
