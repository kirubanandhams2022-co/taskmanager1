package com.taskmanager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class TaskServiceTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TaskRepository taskRepository;

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
        // We need to manually set the repository since we're not using @Autowired in TaskService for tests
        // In a real scenario, you'd use @Mock or proper dependency injection
    }

    @Test
    public void testCreateAndFindTask() {
        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setDueDate(LocalDate.now().plusDays(1));
        task.setCategory(Task.Category.WORK);
        task.setPriority(Task.Priority.HIGH);

        Task savedTask = taskRepository.save(task);

        assertNotNull(savedTask.getId());
        assertEquals("Test Task", savedTask.getTitle());
        assertEquals(Task.Category.WORK, savedTask.getCategory());
        assertEquals(Task.Priority.HIGH, savedTask.getPriority());
    }

    @Test
    public void testFindByCompletedStatus() {
        // Create test tasks
        Task task1 = new Task("Incomplete Task", "Description 1", LocalDate.now().plusDays(1));
        task1.setCompleted(false);

        Task task2 = new Task("Complete Task", "Description 2", LocalDate.now().plusDays(2));
        task2.setCompleted(true);

        taskRepository.save(task1);
        taskRepository.save(task2);

        List<Task> incompleteTasks = taskRepository.findByCompletedFalse();
        List<Task> completeTasks = taskRepository.findByCompletedTrue();

        assertFalse(incompleteTasks.isEmpty());
        assertFalse(completeTasks.isEmpty());
        assertEquals("Incomplete Task", incompleteTasks.get(0).getTitle());
        assertEquals("Complete Task", completeTasks.get(0).getTitle());
    }

    @Test
    public void testTaskCountMethods() {
        // Create test data
        Task task1 = new Task("Task 1", "Desc 1", LocalDate.now().plusDays(1));
        Task task2 = new Task("Task 2", "Desc 2", LocalDate.now().plusDays(2));
        Task task3 = new Task("Task 3", "Desc 3", LocalDate.now().plusDays(3));
        task3.setCompleted(true);

        taskRepository.save(task1);
        taskRepository.save(task2);
        taskRepository.save(task3);

        long totalCount = taskRepository.count();
        long completedCount = taskRepository.countByCompletedTrue();
        long activeCount = taskRepository.countByCompletedFalse();

        assertEquals(3, totalCount);
        assertEquals(1, completedCount);
        assertEquals(2, activeCount);
    }

    @Test
    public void testSearchTasks() {
        Task task1 = new Task("Learn Spring Boot", "Learn Spring Boot framework", LocalDate.now().plusDays(1));
        Task task2 = new Task("Buy groceries", "Milk and eggs", LocalDate.now().plusDays(2));

        taskRepository.save(task1);
        taskRepository.save(task2);

        List<Task> springTasks = taskRepository.searchTasks("spring");
        List<Task> groceryTasks = taskRepository.searchTasks("grocery");

        assertEquals(1, springTasks.size());
        assertEquals(1, groceryTasks.size());
        assertEquals("Learn Spring Boot", springTasks.get(0).getTitle());
    }
}