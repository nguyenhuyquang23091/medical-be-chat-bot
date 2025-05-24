package com.example.chatbox.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AlphaAIConfiguration {

    @Value("${llm.url}")
    private String apiUrl;

    @Bean
    public RestClient restClient(){
        return RestClient.
                builder().
                baseUrl(apiUrl).
                build();
    }

    @Bean
    public RestTemplate restTemplate(){
        return  new RestTemplate();
    }
}
