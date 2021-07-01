package com.donal.jobcoin;

import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class Mixer {

    public boolean mix(String addresses[]) {
        System.out.println("Mix into these addresses " + Arrays.toString(addresses));

        return true;
    }
}
