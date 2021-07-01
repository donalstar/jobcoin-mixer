package com.donal.jobcoin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class HttpClient {
    private static final Logger log = LoggerFactory.getLogger(HttpClient.class);

    public String get() {
        RestTemplate restTemplate = new RestTemplate();

        Quote quote = restTemplate.getForObject(
                "https://quoters.apps.pcfone.io/api/random", Quote.class);

        return quote.toString();
    }
}
