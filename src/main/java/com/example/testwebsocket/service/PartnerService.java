package com.example.testwebsocket.service;

import com.example.testwebsocket.config.ServiceConfig;
import com.example.testwebsocket.dto.currencyExchange.partner.ApilayerCurrencyExchangeIn;
import com.example.testwebsocket.dto.currencyExchange.partner.CurrateCurrencyExchangeIn;
import com.example.testwebsocket.dto.currencyExchange.partner.CurrateCurrencyListIn;
import com.example.testwebsocket.dto.currencyExchange.partner.CurrencyExchangeFromPartner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class PartnerService {

    private final ServiceConfig serviceConfig;
    private final RestTemplate restTemplate;

    private final String URL_CURRATE_ALL_CURRENCIES;
    private final String URL_CURRATE_CURRENCY_EXCHANGE;
    private final String URL_APILAYER_CURRENCY_EXCHANGE;


    public PartnerService(ServiceConfig serviceConfig, RestTemplate restTemplate) {
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

        Map<String, BigDecimal> combinedCurrencyExchange = null;

        try {
            combinedCurrencyExchange = getCompletableFutures().stream()
                    .map(CompletableFuture::join)
                    .filter(optional -> optional.isPresent())
                    .map(optional -> optional.get().getCurrencyExchangeFromPartner())
                    .reduce(new HashMap<>(), (one, two) -> {
                        one.putAll(two);
                        return one;
                    });
        } catch (Exception e) {
            log.error("Can't combine async call", e);
        }

        return combinedCurrencyExchange == null ? new HashMap<>() : combinedCurrencyExchange;
    }

    public List<String> getAvailableCurrencies() {
        CurrateCurrencyListIn currateCurrencyListIn = null;

        try {
            currateCurrencyListIn =
                    restTemplate.getForObject(URL_CURRATE_ALL_CURRENCIES, CurrateCurrencyListIn.class);
        } catch (Exception e) {
            log.error("Can't got CURRATE_ALL_CURRENCIES");
        }

        if (currateCurrencyListIn != null)
            return currateCurrencyListIn.getData();

        return new ArrayList<>();
    }

    public List<CompletableFuture<Optional<CurrencyExchangeFromPartner>>> getCompletableFutures() {

        List<String> currencyList = getAvailableCurrencies();

        String currenciesInLine = String.join(",", currencyList);

        CompletableFuture<Optional<CurrateCurrencyExchangeIn>> currateFeature
                = CompletableFuture.supplyAsync(() -> Optional.of(restTemplate.getForObject(
                String.format(URL_CURRATE_CURRENCY_EXCHANGE, new Object[]{currenciesInLine}),
                CurrateCurrencyExchangeIn.class)));
        CompletableFuture<Optional<ApilayerCurrencyExchangeIn>> apilayerFeature
                = CompletableFuture.supplyAsync(() -> Optional.of(restTemplate.getForObject(URL_APILAYER_CURRENCY_EXCHANGE,
                ApilayerCurrencyExchangeIn.class)));

        return new ArrayList(Arrays.asList(currateFeature, apilayerFeature));
    }

}
