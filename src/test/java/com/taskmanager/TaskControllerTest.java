package com.taskmanager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

// FIX: Added proper imports
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testHomePage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("tasks", "completedTasks"));
    }

    @Test
    public void testAddTask() throws Exception {
        mockMvc.perform(post("/add")
                        .param("title", "Test Task")
                        .param("description", "Test Description")
                        .param("category", "WORK")
                        .param("priority", "HIGH"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    // Additional test methods
    @Test
    public void testCompleteTask() throws Exception {
        // First create a task, then complete it
        mockMvc.perform(post("/add")
                        .param("title", "Task to Complete")
                        .param("description", "Description"))
                .andExpect(status().is3xxRedirection());

        // This would need the actual task ID, simplified for example
        mockMvc.perform(post("/complete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }
}