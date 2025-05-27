package com.example.chatbox.service;

import com.example.chatbox.dto.request.ChatRequest;
import com.example.chatbox.dto.request.LLMRequest;
import com.example.chatbox.dto.response.ChatResponse;
import com.example.chatbox.dto.response.LLMResponse;
import com.example.chatbox.entity.ChatEntity;
import com.example.chatbox.mapper.ChatMapper;
import com.example.chatbox.repository.ChatHistoryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ChatService {

    @Value("${spring.ai.model.default-model}")
    @NonFinal
    String llmModel;

    @Value("${spring.ai.model.temperature}")
    @NonFinal
    double temperature;

    @Value("${spring.ai.model.default_token}")
    @NonFinal
    int token;

    ChatHistoryRepository chatHistoryRepository;
    ChatMapper chatMapper;
    LLMService llmService;

    public ChatResponse createChat(ChatRequest chatRequest) {
        // Handle conversation ID
        String conversationId = handleConversationId(chatRequest.getConversationId());

        // Create messages list
        List<LLMRequest.Message> messages = new ArrayList<>();
        messages.add(LLMRequest.Message.builder()
                .role("system")
                .content("You are a helpful medical assistant")
                .build());

        messages.add(LLMRequest.Message.builder()
                .role("user")
                .content(chatRequest.getContent())
                .build());

        // Create LLM request
        LLMRequest llmRequest = LLMRequest.builder()
                .model(llmModel)
                .messages(messages)
                .temperature(temperature)
                .max_tokens(token)
                .build();

        try {
            // Get LLM response
            log.debug("Sending request to LLM service");
            LLMResponse llmResponse = llmService.getLlmResponse(llmRequest);
            
            if (llmResponse == null || llmResponse.getChoices() == null || llmResponse.getChoices().isEmpty()) {
                throw new RuntimeException("Invalid response from LLM service");
            }

            String assistantContent = llmResponse.getChoices().get(0).getMessage().getContent().trim();

            // Create and save chat entity
            ChatEntity chatEntity = chatMapper.toChatEntity(
                    chatRequest,
                    assistantContent,
                    llmModel,
                    conversationId
            );
            
            ChatEntity savedEntity = chatHistoryRepository.save(chatEntity);
            log.debug("Saved chat entity with ID: {}", savedEntity.getId());
            
            return chatMapper.toChatResponse(savedEntity);

        } catch (Exception e) {
            log.error("Error processing chat request: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to process chat request: " + e.getMessage(), e);
        }
    }

    private String handleConversationId(String conversationId) {
        if (StringUtils.hasText(conversationId) && chatHistoryRepository.existsByConversationId(conversationId)) {
            return conversationId;
        }
        //its value is assigned when we call hanldeConversationId method;
        return UUID.randomUUID().toString();
    }

}
