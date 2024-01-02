package org.sergn.config;

import org.sergn.CryptoTool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestConfiguration {
    @Value("${salt}")
    private String salt;

    @Bean
    public CryptoTool getCryptoTool(){
        return new CryptoTool(salt);
    }
}