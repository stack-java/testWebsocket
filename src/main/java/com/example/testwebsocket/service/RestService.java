package com.example.testwebsocket.service;

import com.example.testwebsocket.config.ServiceConfig;
import com.example.testwebsocket.dto.currencyExchange.partner.ApilayerCurrencyExchangeIn;
import com.example.testwebsocket.dto.currencyExchange.partner.CurrateCurrencyExchangeIn;
import com.example.testwebsocket.dto.currencyExchange.partner.CurrateCurrencyListIn;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class RestService {

    private ServiceConfig serviceConfig;
    private RestTemplate restTemplate;

    private String URL_CURRATE_ALL_CURRENCIES;
    private String URL_CURRATE_CURRENCY_EXCHANGE;
    private String URL_APILAYER_CURRENCY_EXCHANGE;


    public RestService(ServiceConfig serviceConfig, RestTemplate restTemplate) {
        this.serviceConfig = serviceConfig;
        this.restTemplate = restTemplate;

        URL_CURRATE_ALL_CURRENCIES = serviceConfig.getOutUrlCurrateHost() +
                "?get=currency_list&key=" + serviceConfig.getOutUrlCurrateKey();
        URL_CURRATE_CURRENCY_EXCHANGE = serviceConfig.getOutUrlCurrateHost() +
                "?get=rates&pairs=%s&key=" + serviceConfig.getOutUrlCurrateKey();
        URL_APILAYER_CURRENCY_EXCHANGE = serviceConfig.getOutUrlApilayerHost() +
                "/live?access_key=" + serviceConfig.getOutUrlApilayerKey();

    }

    public Map<String, BigDecimal> getCurrencyExchangeFromPartner() {
        List<String> currencyList = getAvailableCurrencies();

        String url = String.join(",", currencyList);

        CurrateCurrencyExchangeIn currateCurrencyExchangeIn = null;
        ApilayerCurrencyExchangeIn apilayerCurrencyExchangeIn = null;

        ForkJoinPool forkJoinPool = new ForkJoinPool(ForkJoinPool.getCommonPoolParallelism());

        CompletableFuture<Optional<CurrateCurrencyExchangeIn>> currateFeature
                = CompletableFuture.supplyAsync(() -> Optional.of(restTemplate.getForObject(
                String.format(URL_CURRATE_CURRENCY_EXCHANGE, new Object[]{url}),
                CurrateCurrencyExchangeIn.class)), forkJoinPool);
        CompletableFuture<Optional<ApilayerCurrencyExchangeIn>> apilayerFeature
                = CompletableFuture.supplyAsync(() -> Optional.of(restTemplate.getForObject(URL_APILAYER_CURRENCY_EXCHANGE,
                ApilayerCurrencyExchangeIn.class)), forkJoinPool);

        try {
            List<Optional> optionals = Stream.of(currateFeature, apilayerFeature)
                        .map(CompletableFuture::join).collect(Collectors.toList());

            for (Optional optional : optionals) {
                if (optional.isPresent()) {
                    Object object = optional.get();
                    if (object instanceof CurrateCurrencyExchangeIn) {
                        currateCurrencyExchangeIn = (CurrateCurrencyExchangeIn) object;
                    }
                    if (object instanceof ApilayerCurrencyExchangeIn) {
                        apilayerCurrencyExchangeIn = (ApilayerCurrencyExchangeIn) object;
                    }
                }
            }
        } catch (Exception e) {
            log.error("Can't combine async call", e);
        }

        return consolidateOtherSources(currateCurrencyExchangeIn, apilayerCurrencyExchangeIn);
    }

    public List<String> getAvailableCurrencies() {

        CurrateCurrencyListIn currateCurrencyListIn =
                restTemplate.getForObject(URL_CURRATE_ALL_CURRENCIES, CurrateCurrencyListIn.class);

        if (currateCurrencyListIn != null)
            return currateCurrencyListIn.getData();

        return new ArrayList<>();
    }

    public Map<String, BigDecimal> consolidateOtherSources(CurrateCurrencyExchangeIn currateCurrencyExchangeIn,
                                                           ApilayerCurrencyExchangeIn apilayerCurrencyExchangeIn) {

        Map<String, BigDecimal> currencyExchangeInMap = new HashMap<>();

        if (currateCurrencyExchangeIn != null
                && currateCurrencyExchangeIn.getData() != null
                && currateCurrencyExchangeIn.getData().size() > 0) {

            currencyExchangeInMap.putAll(currateCurrencyExchangeIn.getData());

        }

        if (apilayerCurrencyExchangeIn != null
                && apilayerCurrencyExchangeIn.getQuotes() != null
                && apilayerCurrencyExchangeIn.getQuotes().size() > 0) {

            currencyExchangeInMap.putAll(apilayerCurrencyExchangeIn.getQuotes());

        }

        return currencyExchangeInMap;

    }


}
