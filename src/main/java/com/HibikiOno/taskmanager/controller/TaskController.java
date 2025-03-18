package com.HibikiOno.taskmanager.controller;

import com.HibikiOno.taskmanager.entity.User;
import com.HibikiOno.taskmanager.entity.Task;
import com.HibikiOno.taskmanager.entity.Task.TaskStatus;
import com.HibikiOno.taskmanager.service.TaskService;
import com.HibikiOno.taskmanager.service.UserService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;


@RestController
@RequestMapping("/api/tasks")
public class TaskController {


    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    //すべてのタスクを取得する
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(){
        return ResponseEntity.ok(taskService.getAllTasks());
    }
    
    // IDを指定して特定のタスクを取得する
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Optional<Task> task = taskService.getTaskById(id);
        return task.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    //特定のユーザーのすべてのタスクを取得
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Task>> getTaskByUserId(@PathVariable Long userId) {
        List<Task> tasks = taskService.getTasksByUserId(userId);
        return ResponseEntity.ok(tasks);
    }
    
    //タスクを締切日順に取得する（昇順or降順:デフォルトは昇順）
    @GetMapping("/sorted")
    public ResponseEntity<List<Task>> getAllTasks(@RequestParam(defaultValue = "asc") String sort) {
        List<Task> tasks = taskService.getAllTasksSorted(sort);
        return ResponseEntity.ok(tasks);
    }
    
    //タスクを締切日で検索する
    @GetMapping("/dueDate")
    public ResponseEntity<List<Task>> getTasksByDueDate(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueDate,
        @AuthenticationPrincipal UserDetails userDetails
    ){
        String username = userDetails.getUsername();
        Optional<User> user = userService.findByUsername(username);        
        
        if(user.isEmpty()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Task> tasks = taskService.getTasksByDueDateAndUser(dueDate, user.get().getId());
        return ResponseEntity.ok(tasks);
    }

    // 新しいタスクを作成する
    @PostMapping
    public ResponseEntity<Task> createTask(@Valid @RequestBody Task task)  {
        Task savedTask = taskService.createTask(task);
        return ResponseEntity.ok(savedTask);
    }


    // 既存のタスクを更新する
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @Valid @RequestBody Task taskDetails) {
        Optional<Task> updatedTask = taskService.updateTask(id, taskDetails);
        return updatedTask.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    //タスクのステータスを更新（実行したかどうか）
    @PatchMapping("/{id}/status")
    public ResponseEntity<Task> updateStatus(@PathVariable Long id, @RequestParam TaskStatus status) {
        Optional<Task> updatedTask = taskService.updateTaskStatus(id, status);
        return updatedTask.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // タスクを削除する
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        if (taskService.deleteTask(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<Task>> getPaginatedTasks(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "dueDate") String sortBy,
        @RequestParam(defaultValue = "asc") String direction){

        Page<Task> tasks = taskService.getTasksPaginated(page,size,sortBy,direction);
        return ResponseEntity.ok(tasks);

        }
    
}
