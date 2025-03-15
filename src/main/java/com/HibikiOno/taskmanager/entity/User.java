package com.HibikiOno.taskmanager.entity;

//import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users") //テーブル名を"users"に指定
@Getter
@Setter 
@NoArgsConstructor

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 3 , max = 20 , message = "ユーザーネームは、3~20文字で入力してください！")
    @Column(nullable = false , unique = true)
    private String username ;

    @Size(min = 6 , message = "パスワードは6文字以上で入力してください！")
    @Column(nullable = false)
    private String password;

}
