package com.example.assistant.dao;

import com.example.assistant.entity.ChatStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatStorageRepository extends JpaRepository<ChatStorage,Long> {

}
