package com.example.chatbox.entity;


import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;


@Getter
@Setter
@Builder
@Document("chat_store")
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatEntity {
    @Id
    String id;
    String modelUsed;
    String conversationId;
    String userMessage;
    String assistantResponse;
    Instant timestamp;

}
