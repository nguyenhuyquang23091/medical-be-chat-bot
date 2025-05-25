package com.example.chatbox.entity;


import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


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
    String chatId;
    String userMessage;
    String assistantResponse;

}
