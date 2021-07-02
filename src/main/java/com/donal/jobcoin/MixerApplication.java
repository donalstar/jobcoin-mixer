package com.donal.jobcoin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;

/**
 * //	java -jar commandline-app-0.0.1-SNAPSHOT.jar --addresses=alpha,beta,gamma
 */
@SpringBootApplication
public class MixerApplication implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(MixerApplication.class);

    @Autowired
    MixerService mixerService;

    public static void main(String[] args) {
        SpringApplication.run(MixerApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        // Get input parameters
        String[] destinationAddresses = inputDepositAddresses(args);

        if (destinationAddresses == null) {
            System.out.println("Welcome to the Jobcoin mixer!\n" +
                    "\tPlease enter a comma-separated list of new, unused Jobcoin addresses\n" +
                    "\twhere your mixed Jobcoins will be sent. Example:\n" +
                    "\n" +
                    "\t./bin/mixer --addresses=bravo,tango,delta");

            System.exit(-1);
        }

        String sourceAddress = getSourceAddress(args);

        // mix coins
        mixerService.mixCoins(sourceAddress, destinationAddresses);

        // get addresses after mixing
        Map<String, String> mixedCoins = mixerService.getMixedCoins(destinationAddresses);

        System.out.println("Mixed coins/amounts " + mixedCoins);
    }

    private String getSourceAddress(ApplicationArguments args) {
        String result = null;

        List<String> values = getValues("source", args);

        if (values != null) {
            result = values.get(0);
        }

        return result;
    }

    /**
     * @return
     */
    private String[] inputDepositAddresses(ApplicationArguments args) {
        String[] results = null;
        List<String> values = getValues("addresses", args);

        if (values != null) {
            String value = values.get(0);

            results = value.split(",");
        }

        return results;
    }

    private List<String> getValues(String name, ApplicationArguments args) {
        List<String> values = null;

        Set<String> names = args.getOptionNames();

        if (names.contains(name)) {
            values = args.getOptionValues(name);
        }

        return values;
    }
}
