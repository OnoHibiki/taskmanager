package com.HibikiOno.taskmanager.service;

import com.HibikiOno.taskmanager.entity.User;
import com.HibikiOno.taskmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//passのハッシュ化
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@Service
@Transactional
public class UserService implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    //ユーザを保存(パスワードをハッシュ化)
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); //ハッシュ化
        return userRepository.save(user);
    }

    //ユーザーを検索
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    //ユーザー名が存在するか確認
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    //パスワードの認証（ログイン用)
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword) ;

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません: " + username));
    
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword()) // すでにハッシュ化されているパスワード
                .roles(user.getRole().name()) // `User` クラスに `getRole()` があることを確認
                .build();
    }

}
