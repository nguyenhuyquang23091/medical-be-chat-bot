package com.example.chatbox.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Configuration
public class AlphaAIConfiguration {

    @Value("${spring.ai.model.base-url}")
    private String apiUrl;

    @Bean
   public RestClient restClient(){
        return RestClient.builder()
                .baseUrl(apiUrl)
                .build();
    }
}
