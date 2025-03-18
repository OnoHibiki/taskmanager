package com.HibikiOno.taskmanager.controller;
import com.HibikiOno.taskmanager.dto.UserRegisterDTO;

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
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/users")
public class UserController {


    @Autowired
    private UserService userService;
    
    //新しいユーザを作成
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegisterDTO userDto , BindingResult result)  {
        if(result.hasErrors()) {
            List<ErrorResponse> errors = result.getFieldErrors().stream()
            .map(error -> new ErrorResponse(error.getField(),error.getDefaultMessage()))
            .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(new ErrorListResponse(errors));
        }
        
        if(userService.existsByUsername(userDto.getUsername())) {
            return ResponseEntity.badRequest().body("そのユーザー名は既に存在します"); 
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());

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

    //エラー文言用クラス
    class ErrorResponse {
        private String field;
        private String message;

        public ErrorResponse(String field, String message) {
            this.field = field;
            this.message = message;
        }

        public String getFiled() { return field ;}
        public String getMessage() { return message; }

    }
    
    class ErrorListResponse {
        private List<ErrorResponse> errors ;
        
        public ErrorListResponse(List<ErrorResponse> errors) {
            this.errors = errors;
        }

        public ErrorListResponse(String field, String message) {
            this.errors = List.of(new ErrorResponse(field, message));
        }

        public List<ErrorResponse> getErrors() { return errors; }
    }

}

