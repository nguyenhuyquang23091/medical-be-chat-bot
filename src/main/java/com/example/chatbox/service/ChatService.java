package com.example.chatbox.service;


import com.example.chatbox.dto.request.ChatRequest;
import com.example.chatbox.dto.request.LLMRequest;
import com.example.chatbox.dto.response.ChatResponse;
import com.example.chatbox.dto.response.LLMResponse;
import com.example.chatbox.entity.ChatEntity;
import com.example.chatbox.mapper.ChatMapper;
import com.example.chatbox.repository.ChatHistoryRepository;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    public ChatResponse createChat(ChatRequest chatRequest, String conversationId){
        //default instruction
        List<LLMRequest.Message> messages = new ArrayList<>();
        messages.add(LLMRequest.Message.builder()
                .role("system")
                .content("You are a helpful medical assistance")
                .build());

        if (chatRequest.getContent() == null || chatRequest.getContent().trim().isEmpty()) {
            log.warn("User input is empty for conversationId: {}", conversationId);
            // Consider how to handle empty input - perhaps return an error ChatResponse early
            return new ChatResponse("Please provide some input.");
        }

        messages.add(LLMRequest.Message.builder()
                .role("user")
                .content(chatRequest.getContent())
                .build());


        LLMRequest llmRequest =
                LLMRequest.builder()
                        .model(llmModel)
                        .messages(messages)
                        .temperature(temperature)
                        .max_tokens(token)
                        .build();
        //default message
        String assistantContent;

        try {
            LLMResponse llmServiceResponse = llmService.getLlmResponse(llmRequest);

            LLMResponse.Choice firstChoice = llmServiceResponse.getChoices().get(0);

            assistantContent = firstChoice.getMessage().getContent().trim(); //get assistant content
        } catch (RuntimeException e) {
            throw new RuntimeException("Error" + e);
        }

        ChatEntity chatEntity = chatMapper.toChatEntity(
                chatRequest
                ,assistantContent
                , llmModel
                ,conversationId
        );
        return chatMapper.toChatResponse(chatHistoryRepository.save(chatEntity));
    }

}
