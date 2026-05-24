package com.example.assistant.dao;

import com.example.assistant.entity.ChatList;
import com.example.assistant.entity.UserData;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatListRepository extends JpaRepository<ChatList,Long> {
    Optional<ChatList> findByUserId(Long userId);

}
