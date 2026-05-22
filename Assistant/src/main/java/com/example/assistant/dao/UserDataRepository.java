package com.example.assistant.dao;

import com.example.assistant.entity.User;
import com.example.assistant.entity.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDataRepository extends JpaRepository<UserData,Long> {
    Optional<UserData> findByUserId(Long userId);
}
