package com.example.chatbox.repository;


import com.example.chatbox.entity.ChatEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;


@Repository
public interface ChatHistoryRepository extends MongoRepository<ChatEntity, String> {
    boolean existsById(String chatId);
    List<ChatEntity> findByChatIdOrderByTimestampDesc(String chatId, Pageable pageable);

}
