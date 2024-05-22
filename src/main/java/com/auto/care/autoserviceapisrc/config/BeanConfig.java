package com.auto.care.autoserviceapisrc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.ZoneId;

@Configuration
public class BeanConfig {

    @Value( "${password.encoder.rounds}" )
    private Integer passwordEncoderRounds;
    @Value( "${time.zone}" )
    private String timeZone;

    @Bean
    public ZoneId zoneId() {
        return ZoneId.of(timeZone);
    }

}
