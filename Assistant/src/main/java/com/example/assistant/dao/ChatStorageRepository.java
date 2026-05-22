package com.example.assistant.dao;

import com.example.assistant.entity.ChatList;
import com.example.assistant.entity.ChatStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatStorageRepository extends JpaRepository<ChatStorage,Long> {
    Optional<ChatStorage> findByUserId(Long userId);
    Optional<ChatStorage> findByUserIdAndCreateTime(Long userId,String createTime);
}
