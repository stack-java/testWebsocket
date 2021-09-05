package com.example.testwebsocket.config;

import lombok.Getter;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Configuration
public class ServiceConfig {

    @Value("${restTimeout}")
    private int restTimeout;

    @Value("${outUrl.apilayer.host}")
    private String outUrlApilayerHost;

    @Value("${outUrl.apilayer.key}")
    private String outUrlApilayerKey;

    @Value("${outUrl.currate.host}")
    private String outUrlCurrateHost;

    @Value("${outUrl.currate.key}")
    private String outUrlCurrateKey;

    @Bean
    public RestTemplate getRestTemplate() {

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
//Add the Jackson Message converter
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();

// Note: here we are making this converter to process any kind of response,
// not only application/*json, which is the default behaviour
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        messageConverters.add(converter);

        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(restTimeout)
                .setConnectionRequestTimeout(restTimeout)
                .setSocketTimeout(restTimeout)
                .build();
        CloseableHttpClient client = HttpClientBuilder
                .create()
                .setDefaultRequestConfig(config)
                .build();

        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory(client));
        restTemplate.setMessageConverters(messageConverters);

        return restTemplate;

    }

}


