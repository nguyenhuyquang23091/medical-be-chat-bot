package com.example.chatbox.service;

import com.example.chatbox.dto.request.LLMRequest;
import com.example.chatbox.dto.response.LLMResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class LLMService {
    RestClient restClient;
    @NonFinal

    public LLMResponse getLlmResponse(LLMRequest llmRequest) {
        try {
            return restClient.post()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(llmRequest)
                    .retrieve()
                    .body(LLMResponse.class);
        } catch (RestClientException e) {
            throw new RuntimeException("Failed to get a response from LLM. Cause: " + e.getMessage(), e);
        }
    }
}
