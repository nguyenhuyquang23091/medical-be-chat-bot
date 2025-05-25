package com.example.chatbox.mapper; // Ensure your package name is correct

import com.example.chatbox.dto.request.ChatRequest;
import com.example.chatbox.dto.response.ChatResponse; // Corrected typo
import com.example.chatbox.entity.ChatEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings; // Import this


@Mapper(componentModel = "spring")
public interface ChatMapper {

    @Mappings({
            @Mapping(source = "chatRequest.content", target = "userMessage"),
            // These sources come from the additional parameters we're adding to the method
            @Mapping(source = "assistantResponseContent", target = "assistantResponse"),
            @Mapping(source = "model", target = "modelUsed"),
            @Mapping(source = "conversationId", target = "chatId"),
            @Mapping(target = "id", ignore = true),
    })

    ChatEntity toChatEntity(
            ChatRequest chatRequest,
            String assistantResponseContent, // LLM's reply text
            String model,                // Model used for the reply
            String conversationId            // ID for the whole chat session
    );


    @Mapping(source = "assistantResponse", target = "assistantMessage")
    ChatResponse toChatResponse(ChatEntity chatEntity);
}