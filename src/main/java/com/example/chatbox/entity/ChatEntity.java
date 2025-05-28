package com.example.chatbox.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

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
    String chatId;
    @Builder.Default
    List<MessageEmbeddable> messages = new ArrayList<>();
    String modelUsed;
    Instant createdAt;
    Instant updatedAt;
}
