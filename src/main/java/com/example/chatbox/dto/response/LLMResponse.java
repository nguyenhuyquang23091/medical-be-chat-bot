package com.example.chatbox.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LLMResponse {

    String id;
    String object;
    long created;
    String model;

    List<Choice> choices;
    Usage usage;

    Object stats; // Added to handle the "stats": {} field.
    // Could also be Map<String, Object> if you prefer.
    // If 'stats' has a known, consistent structure, create a StatsDTO.

    @JsonProperty("system_fingerprint")
    String systemFingerprint;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Choice {
        int index;
        Message message;

        @JsonProperty("finish_reason")
        String finishReason;

        Object logprobs;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Message {
        String role;
        String content; //
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Usage {
        @JsonProperty("prompt_tokens")
        int promptTokens;

        @JsonProperty("completion_tokens")
        int completionTokens;

        @JsonProperty("total_tokens")
        int totalTokens;
    }
}