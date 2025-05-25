package com.example.chatbox.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "messages can't not be blank")
    private List<Message> messages;

    @NotBlank(message = "temperature can't not be blank")
    @JsonProperty("temperature")
    double temperature;

    @NotBlank(message = "max_tokens can't not be blank")
    @JsonProperty("max_tokens")
    int max_tokens;


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Message {
        String role;
        @NotBlank(message = "content can't not be blank")
        String content;
    }
}
