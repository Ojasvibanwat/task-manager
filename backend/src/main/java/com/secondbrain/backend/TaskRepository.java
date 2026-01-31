package com.secondbrain.backend;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class TaskRepository {
    private final FilePersistenceService persistenceService;
    private final List<Task> tasks = new CopyOnWriteArrayList<>();
    private final Map<String, List<Task>> categoryIndex = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> tagIndex = new ConcurrentHashMap<>();

    public TaskRepository(FilePersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @jakarta.annotation.PostConstruct
    public void init() {
        List<Task> loadedTasks = persistenceService.loadAll();
        // Dedup tasks by ID, keeping the latest version (Log-structured)
        Map<String, Task> taskMap = new java.util.LinkedHashMap<>();
        for (Task t : loadedTasks) {
            taskMap.put(t.getId(), t);
        }
        tasks.addAll(taskMap.values());
        tasks.forEach(this::addToIndex);
    }

    public Task updateStatus(String id, String status) {
        for (Task t : tasks) {
            if (t.getId().equals(id)) {
                t.setStatus(status);
                persistenceService.append(t); // Append new state to log
                return t;
            }
        }
        return null;
    }

    private void addToIndex(Task task) {
        String cat = task.getCategory() == null ? "Inbox" : task.getCategory();
        categoryIndex.computeIfAbsent(cat, k -> new CopyOnWriteArrayList<>()).add(task);

        if (task.getTags() != null) {
            for (String tag : task.getTags()) {
                tagIndex.computeIfAbsent(tag, k -> new HashSet<>()).add(task.getId());
            }
        }
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

    public List<Task> findByTag(String tag) {
        Set<String> ids = tagIndex.getOrDefault(tag, new HashSet<>());
        List<Task> result = new ArrayList<>();
        for (Task t : tasks) {
            if (ids.contains(t.getId())) {
                result.add(t);
            }
        }
        return result;
    }

    public List<Task> findByTags(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return new ArrayList<>();
        }
        Set<String> resultIds = new HashSet<>(tagIndex.getOrDefault(tags.get(0), new HashSet<>()));
        for (int i = 1; i < tags.size(); i++) {
            resultIds.retainAll(tagIndex.getOrDefault(tags.get(i), new HashSet<>()));
        }

        List<Task> result = new ArrayList<>();
        for (Task t : tasks) {
            if (resultIds.contains(t.getId())) {
                result.add(t);
            }
        }
        return result;
    }

    public List<Task> findByCategoryAndTags(String category, List<String> tags) {
        if (category == null || category.isEmpty()) {
            return findByTags(tags);
        }
        if (tags == null || tags.isEmpty()) {
            return findByCategory(category);
        }

        // Get category IDs
        List<Task> categoryTasks = categoryIndex.getOrDefault(category, new ArrayList<>());
        Set<String> categoryIds = new HashSet<>();
        for (Task t : categoryTasks) {
            categoryIds.add(t.getId());
        }

        // Get tag intersection IDs
        Set<String> tagIds = new HashSet<>(tagIndex.getOrDefault(tags.get(0), new HashSet<>()));
        for (int i = 1; i < tags.size(); i++) {
            tagIds.retainAll(tagIndex.getOrDefault(tags.get(i), new HashSet<>()));
        }

        // Intersect both
        tagIds.retainAll(categoryIds);

        List<Task> result = new ArrayList<>();
        for (Task t : tasks) {
            if (tagIds.contains(t.getId())) {
                result.add(t);
            }
        }
        return result;
    }

    public List<String> getAllCategories() {
        return new ArrayList<>(categoryIndex.keySet());
    }

    public List<String> getAllTags() {
        List<String> tags = new ArrayList<>(tagIndex.keySet());
        java.util.Collections.sort(tags);
        return tags;
    }
}
