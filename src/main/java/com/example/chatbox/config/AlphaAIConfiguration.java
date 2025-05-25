package com.example.chatbox.config;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import java.time.Duration;

@Configuration
public class AlphaAIConfiguration {

    @Value("${spring.ai.model.base-url}")
    private String apiUrl;

    @Bean
    public RestClient restClient() {
       SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
       requestFactory.setConnectTimeout(Duration.ofSeconds(15));
       requestFactory.setReadTimeout(Duration.ofSeconds(65));
        return RestClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .requestFactory(requestFactory)
                .build();
    }
}
