package com.donal.jobcoin;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Map;


public class MixerServiceTest {

    private MixerService mixerService;

    private HttpClient httpClient = Mockito.mock(HttpClient.class);
    
    @BeforeEach
    public void setUp() {
        mixerService = new MixerService();

        mixerService.setHttpClient(httpClient);

        mixerService.setFeesAddress("fees");
        mixerService.setFeesRate("0.01");
        
        Mockito.when(httpClient.getBalance("test"))
                .thenReturn("100.0");
    }


    @Test
    public void testMixProportions() {
        String destinationAddresses[] = { "a1", "a2" };

        Map<String, Double>  proportions =
                mixerService.getMixProportions(destinationAddresses, 99.0);

        Assertions.assertEquals(49.005, proportions.get("a1"));
        Assertions.assertEquals(49.005, proportions.get("a2"));
        Assertions.assertEquals(0.99, proportions.get("fees"));
    }

}
