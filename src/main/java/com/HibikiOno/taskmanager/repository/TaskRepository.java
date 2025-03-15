package com.HibikiOno.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Sort;

import com.HibikiOno.taskmanager.entity.Task;
import com.HibikiOno.taskmanager.entity.Task.TaskStatus;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>{
    List<Task> findByUserId(Long userId); //ユーザーごとのタスクを取得
    List<Task> findByDueDateBeforeAndStatusNot(LocalDate date, TaskStatus status);//締切日を過ぎたらフラグを変える
    List<Task> findByDueDateAndUserId(LocalDate dueDate, Long userId);
    List<Task> findAllByOrderByDueDateAsc();//締切日昇順
    List<Task> findAllByOrderByDueDateDesc();//締切日降順
    List<Task> findByDueDate(LocalDate dueDate);
}
