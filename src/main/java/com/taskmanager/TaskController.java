package com.taskmanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    // Display all tasks with statistics
    @GetMapping("/")
    public String showTasks(Model model) {
        List<Task> incompleteTasks = taskRepository.findByCompletedFalse();
        List<Task> completedTasks = taskRepository.findByCompletedTrue();
        List<Task> dueTodayTasks = taskRepository.findByDueDateAndCompletedFalse(LocalDate.now());

        // Statistics
        long totalTasks = taskRepository.count();
        long activeTasks = taskRepository.countByCompletedFalse();
        long completedTasksCount = taskRepository.countByCompletedTrue();

        model.addAttribute("tasks", incompleteTasks);
        model.addAttribute("completedTasks", completedTasks);
        model.addAttribute("newTask", new Task());
        model.addAttribute("totalTasks", totalTasks);
        model.addAttribute("activeTasks", activeTasks);
        model.addAttribute("completedTasksCount", completedTasksCount);
        model.addAttribute("todayTasks", dueTodayTasks.size());

        return "index";
    }

    // Add new task
    @PostMapping("/add")
    public String addTask(@RequestParam String title,
                          @RequestParam(required = false) String description,
                          @RequestParam(required = false) LocalDate dueDate,
                          @RequestParam(required = false) Task.Category category,
                          @RequestParam(required = false) Task.Priority priority) {

        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setDueDate(dueDate);
        task.setCategory(category != null ? category : Task.Category.PERSONAL);
        task.setPriority(priority != null ? priority : Task.Priority.MEDIUM);
        task.setCreatedAt(LocalDateTime.now());

        taskRepository.save(task);
        return "redirect:/";
    }

    // Mark task as completed
    @PostMapping("/complete/{id}")
    public String completeTask(@PathVariable Long id) {
        Optional<Task> taskOpt = taskRepository.findById(id);
        if (taskOpt.isPresent()) {
            Task task = taskOpt.get();
            task.setCompleted(true);
            taskRepository.save(task);
        }
        return "redirect:/";
    }

    // Delete task
    @PostMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskRepository.deleteById(id);
        return "redirect:/";
    }

    // Edit task - show edit form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Task> taskOpt = taskRepository.findById(id);
        if (taskOpt.isPresent()) {
            model.addAttribute("task", taskOpt.get());
            return "edit-task";
        }
        return "redirect:/";
    }

    // Update task
    @PostMapping("/update/{id}")
    public String updateTask(@PathVariable Long id,
                             @RequestParam String title,
                             @RequestParam(required = false) String description,
                             @RequestParam(required = false) LocalDate dueDate,
                             @RequestParam(required = false) Task.Category category,
                             @RequestParam(required = false) Task.Priority priority) {

        Optional<Task> taskOpt = taskRepository.findById(id);
        if (taskOpt.isPresent()) {
            Task task = taskOpt.get();
            task.setTitle(title);
            task.setDescription(description);
            task.setDueDate(dueDate);
            if (category != null) task.setCategory(category);
            if (priority != null) task.setPriority(priority);

            taskRepository.save(task);
        }
        return "redirect:/";
    }

    // Mark task as incomplete
    @PostMapping("/reopen/{id}")
    public String reopenTask(@PathVariable Long id) {
        Optional<Task> taskOpt = taskRepository.findById(id);
        if (taskOpt.isPresent()) {
            Task task = taskOpt.get();
            task.setCompleted(false);
            taskRepository.save(task);
        }
        return "redirect:/";
    }

    // Search tasks
    @GetMapping("/search")
    public String searchTasks(@RequestParam String keyword, Model model) {
        List<Task> searchResults = taskRepository.searchTasks(keyword);
        List<Task> completedTasks = taskRepository.findByCompletedTrue();

        model.addAttribute("tasks", searchResults);
        model.addAttribute("completedTasks", completedTasks);
        model.addAttribute("newTask", new Task());
        model.addAttribute("searchKeyword", keyword);
        model.addAttribute("searchResultsCount", searchResults.size());

        return "index";
    }
}