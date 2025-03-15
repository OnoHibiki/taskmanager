package com.HibikiOno.taskmanager.entity;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.FutureOrPresent;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor

public class Task {
    
    @Id //主キー
    @GeneratedValue(strategy = GenerationType.IDENTITY)//オートインクリメント
    private Long id;

    
    @Column(nullable = false)
    @NotBlank(message = "タイトルを入力してください！")
    @Size(max = 100, message = "タイトルは100文字以内で入力してください！")
    private String title;


    @Column
    @Size(max = 500, message = "説明は500文字以内で入力してください！")
    private String description;

    //@FutureOrPresent(message = "過去の日付は選択できません")
    @Column(nullable = false)
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    private TaskStatus status = TaskStatus.PENDING;

    @Column(nullable = false)
    private Long userId; //ユーザーごとのタスク管理のため

    public enum TaskStatus{
        PENDING, IN_PROGRESS, COMPLETED, EXPIRED
    }

    public Task(String title, String description, LocalDate dueDate, Long userId ){
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.userId = userId;
        this.status = TaskStatus.PENDING; //　デフォルトは未着手
    }


}
