package com.HibikiOno.taskmanager.entity;

import jakarta.validation.constraints.Size;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection ;
import java.util.List;


@Entity
@Table(name = "users") //テーブル名を"users"に指定
@Getter
@Setter 
@NoArgsConstructor

public class User implements UserDetails{


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 3 , max = 20 , message = "ユーザーネームは、3~20文字で入力してください！")
    @Column(nullable = false , unique = true)
    private String username ;

    @Size(min = 6 , message = "パスワードは6文字以上で入力してください！")
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;

    public enum Role {
        USER,ADMIN
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> "ROLE_" + role.name());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true ;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}   
