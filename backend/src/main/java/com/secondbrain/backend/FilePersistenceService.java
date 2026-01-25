package com.secondbrain.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class FilePersistenceService {
    private static final String FILE_PATH = "data/tasks.jsonl";
    private final ObjectMapper objectMapper;

    public FilePersistenceService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        ensureFileExists();
    }

    private void ensureFileExists() {
        try {
            Path path = Paths.get(FILE_PATH);
            if (Files.notExists(path)) {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize storage", e);
        }
    }

    public synchronized void append(Task task) {
        try (FileWriter fw = new FileWriter(FILE_PATH, true);
                PrintWriter pw = new PrintWriter(fw)) {
            String json = objectMapper.writeValueAsString(task);
            pw.println(json);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save task", e);
        }
    }

    public synchronized List<Task> loadAll() {
        List<Task> tasks = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(FILE_PATH));
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    Task task = objectMapper.readValue(line, Task.class);
                    tasks.add(task);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load tasks", e);
        }
        return tasks;
    }
}
