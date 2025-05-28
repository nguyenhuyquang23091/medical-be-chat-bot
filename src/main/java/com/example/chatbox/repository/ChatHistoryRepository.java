package com.example.chatbox.repository;

import com.example.chatbox.entity.ChatEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatHistoryRepository extends MongoRepository<ChatEntity, String> {
    Optional<ChatEntity> findByChatId(String chatId);
    boolean existsByChatId(String chatId);
}
