package com.secondbrain.backend;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@CrossOrigin(origins = "http://localhost:5173") // Allow frontend access
public class TaskController {
    private final TaskRepository repository;

    public TaskController(TaskRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return repository.findAll();
    }

    @PostMapping
    public Task createTask(@RequestBody Task task) {
        // Enforce ID and timestamp generation if missing or override
        if (task.getId() == null)
            task.setId(java.util.UUID.randomUUID().toString());
        if (task.getCreatedAt() == null)
            task.setCreatedAt(java.time.LocalDateTime.now());
        if (task.getStatus() == null)
            task.setStatus("TODO");

        return repository.save(task);
    }
}
