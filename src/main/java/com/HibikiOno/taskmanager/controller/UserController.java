package com.HibikiOno.taskmanager.controller;

import com.HibikiOno.taskmanager.entity.User;
import com.HibikiOno.taskmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

import jakarta.validation.Valid;

import java.util.Optional;



@RestController
@RequestMapping("/users")
public class UserController {


    @Autowired
    private UserService userService;
    
    //新しいユーザを作成
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user , BindingResult result)  {
        if(result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        
        if(userService.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest().body("そのユーザー名は既に存在します"); 
        }

        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(savedUser);
    }

    //ユーザーを取得
    @GetMapping("/{username}")
    public ResponseEntity<User> getUser(@PathVariable String username) {
        Optional<User> user = userService.findByUsername(username);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    //ログイン処理
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody User user) {
        Optional<User> existingUser = userService.findByUsername(user.getUsername());

        if (existingUser.isPresent() && userService.checkPassword(user.getPassword(),existingUser.get().getPassword())) {
            return ResponseEntity.ok("ログインに成功しました！");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("ユーザー名かパスワードが間違っています!");
        }
        
    }
    

}

