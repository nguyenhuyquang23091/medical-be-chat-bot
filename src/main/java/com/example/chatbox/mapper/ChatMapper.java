package com.example.chatbox.mapper; // Ensure your package name is correct

import com.example.chatbox.dto.request.ChatRequest;
import com.example.chatbox.dto.response.ChatResponse; // Corrected typo
import com.example.chatbox.entity.ChatEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings; // Import this


@Mapper(componentModel = "spring")
public interface ChatMapper {

    @Mapping(source = "chatEntity.chatId", target = "conversationId")
    @Mapping(source = "assistantContent", target = "assistantMessage")
    ChatResponse toChatResponse(ChatEntity chatEntity, String assistantContent);
}