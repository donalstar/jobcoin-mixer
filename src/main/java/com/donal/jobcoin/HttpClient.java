package com.donal.jobcoin;

import com.donal.jobcoin.domain.AddressInfo;
import com.donal.jobcoin.domain.SendTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class HttpClient {
    private static final Logger log = LoggerFactory.getLogger(HttpClient.class);

    @org.springframework.beans.factory.annotation.Value("${base.url}")
    private String baseUrl;

    @org.springframework.beans.factory.annotation.Value("${addresses.endpoint}")
    private String addressesEndpoint;

    @org.springframework.beans.factory.annotation.Value("${transaction.endpoint}")
    private String transactionEndpoint;

    /**
     *
     * @param address
     * @return
     */
    public AddressInfo getAddressInfo(String address) {
        log.info("Get address info for " + address);

        RestTemplate restTemplate = new RestTemplate();

        String url = addressesEndpoint + "/" + address;

        return restTemplate.getForObject(url, AddressInfo.class);
    }

    /**
     *
     * @param address
     * @return
     */
    public String getBalance(String address) {
        log.info("Get balance for " + address);

        AddressInfo addressInfo = getAddressInfo(address);

        String balance = (addressInfo == null) ? null : addressInfo.getBalance();

        log.info("Got balance: " + balance);

        return balance;
    }

    /**
     *
     * @param sourceAddress
     * @param depositAddress
     * @param amount
     */
    public void transferToDepositAddress(String sourceAddress, String depositAddress, String amount) {
        log.info("Transfer from: " + sourceAddress + " to: " + depositAddress);

        SendTransaction transaction = new SendTransaction();

        transaction.setFromAddress(sourceAddress);
        transaction.setToAddress(depositAddress);
        transaction.setAmount(amount);

        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<SendTransaction> request = new HttpEntity<>(transaction);

        restTemplate.postForObject(transactionEndpoint, request, SendTransaction.class);
    }
}
