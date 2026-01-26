package com.secondbrain.backend;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class TaskRepository {
    private final FilePersistenceService persistenceService;
    private final List<Task> tasks = new CopyOnWriteArrayList<>();
    private final Map<String, List<Task>> categoryIndex = new ConcurrentHashMap<>();

    public TaskRepository(FilePersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @jakarta.annotation.PostConstruct
    public void init() {
        List<Task> loadedTasks = persistenceService.loadAll();
        tasks.addAll(loadedTasks);
        loadedTasks.forEach(this::addToIndex);
    }

    private void addToIndex(Task task) {
        String cat = task.getCategory() == null ? "Inbox" : task.getCategory();
        categoryIndex.computeIfAbsent(cat, k -> new CopyOnWriteArrayList<>()).add(task);
    }

    public List<Task> findAll() {
        return new ArrayList<>(tasks);
    }

    public Task save(Task task) {
        tasks.add(task);
        addToIndex(task);
        persistenceService.append(task);
        return task;
    }

    public List<Task> findByCategory(String category) {
        return new ArrayList<>(categoryIndex.getOrDefault(category, new ArrayList<>()));
    }
}
