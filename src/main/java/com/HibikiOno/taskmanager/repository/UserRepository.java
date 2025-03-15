package com.HibikiOno.taskmanager.repository;

import com.HibikiOno.taskmanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username); //ユーザーをユーザネームで検索
    boolean existsByUsername(String username); //ユーザネームの重複チェック    
}


