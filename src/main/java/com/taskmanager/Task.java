package com.taskmanager;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    // FIX: Removed duplicate 'user' field and kept the correct one
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String description;

    private LocalDate dueDate;

    private boolean completed = false;

    //NEW FIELDS
    @Enumerated(EnumType.STRING)
    private Category category = Category.PERSONAL;

    @Enumerated(EnumType.STRING)
    private Priority priority = Priority.MEDIUM;

    private LocalDateTime createdAt;

    // Enums for Category and Priority
    public enum Category {
        PERSONAL, WORK, SHOPPING, HEALTH, EDUCATION
    }

    public enum Priority {
        LOW, MEDIUM, HIGH, URGENT
    }

    // Constructors
    public Task() {
        this.createdAt = LocalDateTime.now();
    }

    public Task(String title, String description, LocalDate dueDate) {
        this();
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
    }

    public Task(String title, String description, LocalDate dueDate,
                Category category, Priority priority) {
        this();
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.category = category;
        this.priority = priority;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}