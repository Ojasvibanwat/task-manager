package com.secondbrain.backend;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class TaskRepository {
    // In-memory storage only (per Feature 2 requirements)
    private final List<Task> tasks = new CopyOnWriteArrayList<>();

    public List<Task> findAll() {
        return new ArrayList<>(tasks);
    }

    public Task save(Task task) {
        tasks.add(task);
        return task;
    }
}
