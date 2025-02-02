package com.example.tasktrackerclidemo.service;

import com.example.tasktrackerclidemo.entity.Task;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {
    private static final String JSON_FILE = "tasks.json";
    private final ObjectMapper objectMapper;

    public TaskService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    private List<Task> readTasks() throws IOException {
        if (!Files.exists(Paths.get(JSON_FILE))) {
            return new ArrayList<>();
        }
        File file = new File(JSON_FILE);
        if (file.length() == 0) {
            return new ArrayList<>();
        }
        return objectMapper.readValue(file, new TypeReference<List<Task>>() {});
    }

    private void writeTasks(List<Task> tasks) throws IOException {
        objectMapper.writeValue(new File(JSON_FILE), tasks);
    }

    private int getNextId(List<Task> tasks) {
        return tasks.stream()
                .mapToInt(Task::getId)
                .max()
                .orElse(0) + 1;
    }

    public Task addTask(String description) throws IOException {
        List<Task> tasks = readTasks();
        Task newTask = new Task(getNextId(tasks), description);
        tasks.add(newTask);
        writeTasks(tasks);
        return newTask;
    }

    public boolean updateTask(int id, String newDescription) throws IOException {
        List<Task> tasks = readTasks();
        for (Task task : tasks) {
            if (task.getId() == id) {
                task.setDescription(newDescription);
                task.setUpdatedAt(LocalDateTime.now());
                writeTasks(tasks);
                return true;
            }
        }
        return false;
    }

    public boolean deleteTask(int id) throws IOException {
        List<Task> tasks = readTasks();
        boolean removed = tasks.removeIf(task -> task.getId() == id);
        if (removed) {
            writeTasks(tasks);
        }
        return removed;
    }

    public boolean updateTaskStatus(int id, String status) throws IOException {
        List<Task> tasks = readTasks();
        for (Task task : tasks) {
            if (task.getId() == id) {
                task.setStatus(status);
                task.setUpdatedAt(LocalDateTime.now());
                writeTasks(tasks);
                return true;
            }
        }
        return false;
    }

    public List<Task> listTasks(String status) throws IOException {
        List<Task> tasks = readTasks();
        if (status == null) {
            return tasks;
        }
        return tasks.stream()
                .filter(task -> task.getStatus().equals(status))
                .collect(Collectors.toList());
    }
}