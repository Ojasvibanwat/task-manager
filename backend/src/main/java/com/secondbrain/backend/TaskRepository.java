package com.secondbrain.backend;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class TaskRepository {
    private final FilePersistenceService persistenceService;
    private final List<Task> tasks = new CopyOnWriteArrayList<>();

    public TaskRepository(FilePersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @jakarta.annotation.PostConstruct
    public void init() {
        tasks.addAll(persistenceService.loadAll());
    }

    public List<Task> findAll() {
        return new ArrayList<>(tasks);
    }

    public Task save(Task task) {
        tasks.add(task);
        persistenceService.append(task);
        return task;
    }
}
