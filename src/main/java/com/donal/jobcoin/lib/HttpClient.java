package com.donal.jobcoin.lib;

import com.donal.jobcoin.Quote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class HttpClient {
    private static final Logger log = LoggerFactory.getLogger(HttpClient.class);

    @org.springframework.beans.factory.annotation.Value("${BaseURL}")
    private String baseUrl;

    @org.springframework.beans.factory.annotation.Value("${AddressesEndpoint}")
    private String addressesEndpoint;

    @org.springframework.beans.factory.annotation.Value("${TransactionEndpoint}")
    private String transactionEndpoint;

    public String get() {

        System.out.println("Got resource " + baseUrl);
        System.out.println("Got resource " + addressesEndpoint);
        System.out.println("Got resource " + transactionEndpoint);

        RestTemplate restTemplate = new RestTemplate();

        Quote quote = restTemplate.getForObject(
                "https://quoters.apps.pcfone.io/api/random", Quote.class);

        return quote.toString();
    }
}
