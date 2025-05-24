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
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class LLMService {

    RestTemplate restTemplate;

    @Value("${spring.ai.model.base-url}")
    @NonFinal
    protected String llmApiurl;

    public LLMResponse getLlmResponse(LLMRequest request){
        log.info("Sending request {}", llmApiurl);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LLMRequest> entity = new HttpEntity<>(request, headers);

        try{
            LLMResponse response = restTemplate.postForObject(llmApiurl, entity, LLMResponse.class);
            return  response;

        } catch (RestClientException e) {
            log.error("Error while calling LLM service at URL [{}]. Message: {}", llmApiurl, e.getMessage(), e); // Logs stack trace too
            throw new RuntimeException("Failed to get a response from LLM. Cause: " + e.getMessage(), e);
        }
    }
}
