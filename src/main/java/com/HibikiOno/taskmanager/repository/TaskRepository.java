package com.HibikiOno.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.HibikiOno.taskmanager.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>{
    
}
