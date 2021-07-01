package com.donal.jobcoin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * //	java -jar commandline-app-0.0.1-SNAPSHOT.jar --addresses=alpha,beta,gamma
 */
@SpringBootApplication
public class MixerApplication implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(MixerApplication.class);

    @Autowired
    HttpClient httpClient;

    @Autowired
    Mixer mixer;

    public static void main(String[] args) {
        SpringApplication.run(MixerApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        String[] destinationAddresses = inputDepositAddresses(args);

        if (destinationAddresses == null) {
            System.out.println("Welcome to the Jobcoin mixer!\n" +
                    "\tPlease enter a comma-separated list of new, unused Jobcoin addresses\n" +
                    "\twhere your mixed Jobcoins will be sent. Example:\n" +
                    "\n" +
                    "\t./bin/mixer --addresses=bravo,tango,delta");

            System.exit(-1);
        }

        UUID depositAddress = UUID.randomUUID();

        log.info("You may now send Jobcoins to address " + depositAddress +
                        " and sent to your destination addresses "
                + Arrays.toString(destinationAddresses));

        String response = null;
        try {
            response = httpClient.get();
        } catch (Exception e) {
            log.error("Fatal error", e);
        }

        log.info("HTTP response = " + response);

        mixer.mix(destinationAddresses);
    }

    /**
     * @return
     */
    private String[] inputDepositAddresses(ApplicationArguments args) {
        String[] results = null;

        Set<String> names = args.getOptionNames();

        if (names.contains("addresses")) {
            List<String> addresses = args.getOptionValues("addresses");

            System.out.println("Got addresses " + addresses);

            if (addresses != null) {
                results = new String[addresses.size()];
                addresses.toArray(results); // fill the array
            }
        }

        return results;
    }

}
