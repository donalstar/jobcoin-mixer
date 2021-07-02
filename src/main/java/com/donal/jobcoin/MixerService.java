package com.donal.jobcoin;

import com.donal.jobcoin.domain.AddressInfo;
import com.donal.jobcoin.domain.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class MixerService {
    private static final Logger log = LoggerFactory.getLogger(MixerService.class);

    @Autowired
    HttpClient httpClient;

    @org.springframework.beans.factory.annotation.Value("${fees.address}")
    private String feesAddress;

    @org.springframework.beans.factory.annotation.Value("${fees.rate}")
    private String feesRate;

    /**
     *
     * @param sourceAddress
     * @param destinationAddresses
     */
    public void mixCoins(String sourceAddress, String[] destinationAddresses) {
        log.info("Mix coins");

        String depositAddress = UUID.randomUUID().toString();

        // transfer coins from source -> deposit address
        double numberToBeMixed = transferAllCoins(sourceAddress, depositAddress);

        if (numberToBeMixed == 0.0) {
            log.warn("There are no coins at the source address: " + sourceAddress);
            return;
        }

        // after transfer completion,
        // mix coins - withdraw from deposit address to destination addresses (add fee?)
        Map<String, Double> mixProportions = getMixProportions(destinationAddresses, numberToBeMixed);

        for (String targetAddress : mixProportions.keySet()) {
            Double coinAmount = mixProportions.get(targetAddress);

            transferCoins(depositAddress, targetAddress, coinAmount.toString());
        }

        log.info("Mixing complete");
    }

    /**
     * Determine the number of coins to allocate to each address
     *
     * @return
     */
    public Map<String, Double> getMixProportions(String[] destinationAddresses, double numberToBeMixed) {
        double balance = numberToBeMixed;

        Double fee = null;

        if (feesRate != null) {
            fee = balance * Double.parseDouble(feesRate);

            balance -= fee;
        }

        double allocation = balance / destinationAddresses.length;

        Map<String, Double> coinAllocations = new HashMap<String, Double>();

        if (feesRate != null) {
            coinAllocations.put(feesAddress, fee);
        }

        for (String address : destinationAddresses) {
            coinAllocations.put(address, allocation);
            balance -= allocation;
        }

        // deposit any remaining into the 1st dest address
        coinAllocations.put(destinationAddresses[0], allocation + balance);

        return coinAllocations;
    }

    /**
     *
     * @param destinationAddresses
     * @return
     */
    public Map<String, String> getMixedCoins(String[] destinationAddresses) {

        Map<String, String> mixedCoins = new HashMap<String, String>();

        for (String address : destinationAddresses) {
              AddressInfo addressInfo = httpClient.getAddressInfo(address);

            mixedCoins.put(address, addressInfo.getBalance());
        }

        return mixedCoins;
    }

    /**
     *
     * @param sourceAddress
     * @param targetAddress
     * @param amount
     * @return
     */
    private double transferCoins(String sourceAddress, String targetAddress, String amount) {
        double coinAmount = Double.parseDouble(amount);

        if (coinAmount > 0.0) {
            httpClient.transferToDepositAddress(sourceAddress, targetAddress, amount);

            AddressInfo addressInfo = httpClient.getAddressInfo(targetAddress);

            // Maybe: add in a poller to check whether transfer was completed?
            // https://stackoverflow.com/questions/40257137/java-polling-5-times-after-every-5-secs
            Transaction transactions[] = addressInfo.getTransactions();

            if (transactions.length > 0 && transactions[0].getFromAddress().equals(sourceAddress)) {
                log.info("Transfer successful");
            } else {
                log.info("Transfer not complete...");
            }
        }

        return coinAmount;
    }

    /**
     * @param sourceAddress
     * @param targetAddress
     * @return
     */
    private double transferAllCoins(String sourceAddress, String targetAddress) {
        String balance = httpClient.getBalance(sourceAddress);

        return transferCoins(sourceAddress, targetAddress, balance);
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public void setFeesAddress(String feesAddress) {
        this.feesAddress = feesAddress;
    }

    public void setFeesRate(String feesRate) {
        this.feesRate = feesRate;
    }
}
