package com.secondbrain.backend;

import org.springframework.web.bind.annotation.*;
// import org.springframework.web.bind.annotation.RequestParam; // Already included in .* but good to be explicit or just leave it since .* covers it.
// Actually imports are: import org.springframework.web.bind.annotation.*;
// So RequestParam is covered. I will skip this edit if it's already covered by glob.
// Wait, `import org.springframework.web.bind.annotation.*;` is line 3.
// So I don't need to add it.

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
    public List<Task> getAllTasks(@RequestParam(required = false) String category,
            @RequestParam(required = false) String tag) {
        if (category != null && !category.isEmpty()) {
            return repository.findByCategory(category);
        }
        if (tag != null && !tag.isEmpty()) {
            return repository.findByTag(tag);
        }
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
