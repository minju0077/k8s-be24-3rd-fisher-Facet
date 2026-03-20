package com.facet.api.config.oauth2;

import io.portone.sdk.server.payment.PaymentClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PortoneConfig {

    @Value("${PORTONE_SECRET}")
    private String apiSecret;

    @Bean
    public PaymentClient importClient() {

        return new PaymentClient(apiSecret, "https://api.portone.io", null);
    }
}
