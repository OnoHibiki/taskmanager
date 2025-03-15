package com.HibikiOno.taskmanager.service;

import com.HibikiOno.taskmanager.entity.Task;
import com.HibikiOno.taskmanager.entity.Task.TaskStatus;
import com.HibikiOno.taskmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    //タスクを作成
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    //IDでタスクを取得
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    //全てのタスクを取得
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    //タスクを昇順または降順で取得
    public List<Task> getAllTasksSorted(String sortDirection) {
        if("desc".equalsIgnoreCase(sortDirection)) {
            return taskRepository.findAllByOrderByDueDateDesc();
        }
        return taskRepository.findAllByOrderByDueDateAsc();
    }

    //ユーザーごとのタスク取得
    public List<Task> getTasksByUserId(Long userId) {
        return taskRepository.findByUserId(userId);
    }

    //指定した締切日のタスクを取得
    public List<Task> getTasksByDueDateAndUser(LocalDate dueDate, Long userId) {
        return taskRepository.findByDueDateAndUserId(dueDate, userId);
    }

    //タスクをidを指定して更新
    public Optional<Task> updateTask(Long id , Task taskDetails) {
        return taskRepository.findById(id).map(existingTask -> {
            existingTask.setTitle(taskDetails.getTitle());
            existingTask.setDescription(taskDetails.getDescription());
            existingTask.setDueDate(taskDetails.getDueDate());
            existingTask.setStatus(taskDetails.getStatus());
            return taskRepository.save(existingTask);
        });
    }

    //タスクのステータスを更新するメソッド
    public Optional<Task> updateTaskStatus(Long id, TaskStatus status) {
        return taskRepository.findById(id).map(existingTask -> {
            existingTask.setStatus(status);
            return taskRepository.save(existingTask);
        });
    }

    //タスクを削除
    public boolean deleteTask(Long id) {
        return taskRepository.findById(id).map(task -> {
            taskRepository.delete(task);
            return true;
        }).orElse(false);
    }

    //期限切れのタスクの処理(毎日0時に実行)
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateExpiredTasks() {
        LocalDate today = LocalDate.now();
        List<Task> expiredTasks = taskRepository.findByDueDateBeforeAndStatusNot(today, TaskStatus.EXPIRED);

        for (Task task : expiredTasks) {
            task.setStatus(TaskStatus.EXPIRED);
            taskRepository.save(task);
        }

        System.out.println(expiredTasks.size() + "件のタスクを期限切れに更新しました");
    }
}