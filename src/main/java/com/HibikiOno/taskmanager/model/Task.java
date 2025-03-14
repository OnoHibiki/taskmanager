package com.HibikiOno.taskmanager.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tasks")

public class Task {
    
    @Id //主キー
    @GeneratedValue(strategy = GenerationType.IDENTITY)//オートインクリメント
    private Long id;

    //nullを許容しない
    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    //デフォルトコンストラクタ
    public Task(){}

    public Task(String title, String description){
        this.title = title;
        this.description = description;
    }


    //ゲッター・セッター
    public long getid() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
