package com.example.assistant.dao;

import com.example.assistant.entity.ChatList;
import com.example.assistant.entity.ChatStorage;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatStorageRepository extends JpaRepository<ChatStorage,Long> {
    Optional<ChatStorage> findByUserId(Long userId);
    Optional<ChatStorage> findByUserIdAndCreateTime(Long userId,String createTime);

    @Modifying
    @Transactional
    void deleteByUserIdAndCreateTime(Long userId, String createTime);
}
