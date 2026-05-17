package com.example.assistant.dao;

import com.example.assistant.entity.ChatList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatListRepository extends JpaRepository<ChatList,Long> {

}
