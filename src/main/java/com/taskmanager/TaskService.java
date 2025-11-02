package com.taskmanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public List<Task> getActiveTasks() {
        return taskRepository.findByCompletedFalse();
    }

    public List<Task> getCompletedTasks() {
        return taskRepository.findByCompletedTrue();
    }

    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public List<Task> getTasksDueToday() {
        return taskRepository.findByDueDateAndCompletedFalse(LocalDate.now());
    }

    public long getTotalTaskCount() {
        return taskRepository.count();
    }

    public long getCompletedTaskCount() {
        return taskRepository.countByCompletedTrue();
    }

    public long getActiveTaskCount() {
        return taskRepository.countByCompletedFalse();
    }
}