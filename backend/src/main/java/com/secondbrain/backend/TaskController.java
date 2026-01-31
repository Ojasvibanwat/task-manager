package com.secondbrain.backend;

import org.springframework.web.bind.annotation.*;
// import org.springframework.web.bind.annotation.RequestParam; // Already included in .* but good to be explicit or just leave it since .* covers it.
// Actually imports are: import org.springframework.web.bind.annotation.*;
// So RequestParam is covered. I will skip this edit if it's already covered by glob.
// Wait, `import org.springframework.web.bind.annotation.*;` is line 3.
// So I don't need to add it.

import java.util.ArrayList;
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
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) List<String> tags,
            @RequestParam(required = false) String status) {

        List<Task> result;
        // Normalize tags
        List<String> searchTags = new ArrayList<>();
        if (tags != null) {
            searchTags.addAll(tags);
        }
        if (tag != null && !tag.isEmpty()) {
            searchTags.add(tag);
        }

        if (category != null && !category.isEmpty() && !searchTags.isEmpty()) {
            result = repository.findByCategoryAndTags(category, searchTags);
        } else if (category != null && !category.isEmpty()) {
            result = repository.findByCategory(category);
        } else if (!searchTags.isEmpty()) {
            result = repository.findByTags(searchTags);
        } else {
            result = repository.findAll();
        }

        // Apply Status Filter
        if (status != null && !status.isEmpty()) {
            List<Task> filtered = new ArrayList<>();
            for (Task t : result) {
                if (status.equalsIgnoreCase(t.getStatus())) {
                    filtered.add(t);
                }
            }
            return filtered;
        }

        return result;
    }

    @PostMapping
    public Task createTask(@RequestBody Task task) {
        // Enforce ID and timestamp generation if missing or override
        if (task.getId() == null)
            task.setId(java.util.UUID.randomUUID().toString());
        if (task.getCreatedAt() == null)
            task.setCreatedAt(java.time.LocalDateTime.now());
        if (task.getStatus() == null)
            task.setStatus("OPEN");

        return repository.save(task);
    }

    @PutMapping("/{id}/status")
    public Task updateStatus(@PathVariable String id, @RequestBody java.util.Map<String, String> payload) {
        String status = payload.get("status");
        return repository.updateStatus(id, status);
    }

    @GetMapping("/categories")
    public List<String> getCategories() {
        return repository.getAllCategories();
    }

    @GetMapping("/tags")
    public List<String> getAllTags() {
        return repository.getAllTags();
    }
}
