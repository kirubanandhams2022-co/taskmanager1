package com.taskmanager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByCompletedFalse();
    List<Task> findByCompletedTrue();
    List<Task> findByDueDateAndCompletedFalse(LocalDate dueDate);
    long countByCompletedTrue();
    long countByCompletedFalse();

    // Advanced queries
    List<Task> findByCategory(Task.Category category);
    List<Task> findByPriority(Task.Priority priority);
    List<Task> findByDueDateBeforeAndCompletedFalse(LocalDate date);

    @Query("SELECT t FROM Task t WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Task> searchTasks(String keyword);

    // Find overdue tasks
    @Query("SELECT t FROM Task t WHERE t.dueDate < CURRENT_DATE AND t.completed = false")
    List<Task> findOverdueTasks();

    // Find tasks by category and priority
    List<Task> findByCategoryAndCompletedFalse(Task.Category category);
    List<Task> findByPriorityAndCompletedFalse(Task.Priority priority);
}