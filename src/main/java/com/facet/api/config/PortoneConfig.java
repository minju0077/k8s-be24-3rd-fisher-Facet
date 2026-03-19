package com.facet.api.config;

import io.portone.sdk.server.payment.PaymentClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


// 버리는 코드 나중에 포인트와 합쳐질 예정
@Configuration
public class PortoneConfig {

    @Value("${portone.portone_secret}")
    private String apiSecret;

    @Bean
    public PaymentClient iamportClient() {

        return new PaymentClient(apiSecret, "https://api.portone.io", null);
    }
}
