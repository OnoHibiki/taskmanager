package com.HibikiOno.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.HibikiOno.taskmanager.entity.Task;
import com.HibikiOno.taskmanager.entity.Task.TaskStatus;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>{
    List<Task> findByUserId(Long userId); //ユーザーごとのタスクを取得

    List<Task> findByDueDateBeforeAndStatusNot(LocalDate date, TaskStatus status);
    
}
