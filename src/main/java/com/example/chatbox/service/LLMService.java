package com.example.chatbox.service;


import com.example.chatbox.dto.request.LLMRequest;
import com.example.chatbox.dto.response.LLMResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class LLMService {
    RestClient restClient;

    @NonFinal
    @Value("${spring.ai.model.base-url}")
    protected String llmApiurl;

    public LLMResponse getLlmResponse(LLMRequest request){
        try{
            LLMResponse llmResponse =   restClient.post()
                    .header("Content-Type", "application/json")
                    .body(request)
                    .retrieve()
                    .body(LLMResponse.class);
            return llmResponse;

        } catch (RestClientException e) {
            log.error("Error while calling LLM service at URL [{}]. Message: {}", llmApiurl, e.getMessage(), e); // Logs stack trace too
            throw new RuntimeException("Failed to get a response from LLM. Cause: " + e.getMessage(), e);
        }
    }
}
