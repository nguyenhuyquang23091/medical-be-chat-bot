package com.example.chatbox.service;

import com.example.chatbox.dto.request.ChatRequest;
import com.example.chatbox.dto.request.LLMRequest;
import com.example.chatbox.dto.response.ChatResponse;
import com.example.chatbox.dto.response.LLMResponse;
import com.example.chatbox.entity.ChatEntity;
import com.example.chatbox.entity.MessageEmbeddable;
import com.example.chatbox.mapper.ChatMapper;
import com.example.chatbox.repository.ChatHistoryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Instant;
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
    @Value("${spring.app.chat.history.max-turns}")
    @NonFinal
    int maxTurns;

    ChatHistoryRepository chatHistoryRepository;
    ChatMapper chatMapper;
    LLMService llmService;

    @Transactional
    public ChatResponse createChat (ChatRequest chatRequest) {
        // Handle conversation ID
        Instant requestTimestamp = Instant.now();

        ConversationContext conversationContext = handleConversationId(chatRequest.getConversationId(), requestTimestamp);
        ChatEntity entity = conversationContext.entity;

        MessageEmbeddable currentUserMessage =
                MessageEmbeddable.builder()
                        .role("user")
                        .content(chatRequest.getContent())
                        .timestamp(requestTimestamp)
                        .build();


        // Create messages list
        List<LLMRequest.Message> messages = new ArrayList<>();
        messages.add(LLMRequest.Message.builder()
                .role("system")
                .content("You are a helpful assistant " +
                        "that explains cardiovascular " +
                        "symptoms and answers patient concerns in layman's terms.")
                .build());

        //prepare context ( history for llm)
        List<MessageEmbeddable> messageEmbeddables = entity.getMessages();
        if(!messageEmbeddables.isEmpty()   && maxTurns > 0 ){
            int historySize = messageEmbeddables.size();
            int startIndex = Math.max(0, historySize - maxTurns);
            List<MessageEmbeddable> contextHistory = messageEmbeddables.subList(startIndex, historySize);

            for (MessageEmbeddable msg : contextHistory) {
                messages.add(LLMRequest
                        .Message
                        .builder()
                        .role(msg.getRole())
                        .content(msg.getContent())
                        .build());
            }
        }

        messages.add(LLMRequest.Message
                .builder().
                role("user")
                .content(currentUserMessage.getContent())
                .build());


        // Create LLM request
        LLMRequest llmRequest = LLMRequest.builder()
                .model(llmModel)
                .messages(messages)
                .temperature(temperature)
                .max_tokens(token)
                .build();

        String assistantContent;
        Instant assistantTimestamp;
        try {
            // Get LLM response
            log.debug("Sending request to LLM service");
            LLMResponse llmResponse = llmService.getLlmResponse(llmRequest);

            if (llmResponse == null || llmResponse.getChoices() == null || llmResponse.getChoices().isEmpty()) {
                throw new RuntimeException("Invalid response from LLM service");
            }

            assistantContent = llmResponse.getChoices().get(0).getMessage().getContent().trim();
            assistantTimestamp = Instant.now();


            //for db purpose
            entity.getMessages().add(currentUserMessage);
            MessageEmbeddable assistantMessageEmbeddable =
                    MessageEmbeddable.builder()
                            .role("assistant")
                            .content(assistantContent)
                            .timestamp(assistantTimestamp)
                            .build();
            entity.getMessages().add(assistantMessageEmbeddable);

            ChatEntity savedConversation = chatHistoryRepository.save(entity);

            //For response to user purpose
            return chatMapper.toChatResponse(savedConversation, assistantContent);

        } catch (Exception e) {
            log.error("Error processing chat request: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to process chat request: " + e.getMessage(), e);
        }
    }


    private record ConversationContext(ChatEntity entity, boolean isNew) {}
    private ConversationContext handleConversationId(String clientProvidedChatId, Instant requestTimestamp) {
       if(StringUtils.hasText(clientProvidedChatId)){
           ChatEntity existingChat = chatHistoryRepository.findByChatId(clientProvidedChatId).orElseThrow( () ->
                   new RuntimeException("Cant found")
           );
           return  new ConversationContext(existingChat, false);
       } else {
           String newChatId = UUID.randomUUID().toString();
           ChatEntity newEntity = ChatEntity
                   .builder()
                   .chatId(newChatId)
                   .messages(new ArrayList<>())
                   .createdAt(requestTimestamp)
                   .build();
           return  new ConversationContext(newEntity, true);
       }
    }

}
