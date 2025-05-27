package com.example.chatbox.controller;



import com.example.chatbox.dto.request.ChatRequest;
import com.example.chatbox.dto.response.ChatResponse;
import com.example.chatbox.service.ChatService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chatbot")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ChatBotController {

    ChatService chatService;
    @PostMapping
    public ResponseEntity<ChatResponse> handleChatRequest(@Valid @RequestBody ChatRequest chatRequest){
        try {
            ChatResponse response = chatService.createChat(chatRequest);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            // Log the exception and return an appropriate error response
            // You might want to return a more specific error DTO here
            // For now, returning a generic error message in ChatResponse or just an HTTP status
            return ResponseEntity.status(HttpStatus.NOT_FOUND) // Or HttpStatus.BAD_REQUEST
                    .body(ChatResponse.builder()
                            .assistantMessage(e.getMessage()) // Return the specific error
                            .build());
        }
    }

}
