package com.example.chatbox.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LLMRequest {
    private String model;

    @NotNull(message = "messages can't not be blank")
    private List<Message> messages;

    @NotNull(message = "temperature can't not be blank")
    @JsonProperty("temperature")
    double temperature;

    @NotNull(message = "max_tokens can't not be blank")
    @JsonProperty("max_tokens")
    int max_tokens;


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Message {
        String role;
        @NotNull(message = "content can't not be blank")
        String content;
    }
}
