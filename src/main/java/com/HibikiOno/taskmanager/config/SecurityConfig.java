package com.HibikiOno.taskmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) //開発中のみ無効化
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() //全てのリクエストを認証不要にする
            )
            .formLogin(form -> form.disable())//ログイン画面を無効化
            .httpBasic(httpBasic -> httpBasic.disable()); //Basic認証を無効化
        return http.build();
    }


}
