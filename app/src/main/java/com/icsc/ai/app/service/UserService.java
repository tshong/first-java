package com.icsc.ai.app.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icsc.ai.app.model.User;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Slf4j
@Service
public class UserService {
    private static final String JSON_FILE_PATH = "users.json";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private List<User> users = new ArrayList<>();

    @PostConstruct
    public void init() {
        File file = new File(JSON_FILE_PATH);
        if (!file.exists()) {
            generateRandomUsers();
            saveToFile();
        } else {
            loadFromFile();
        }
    }

    private void generateRandomUsers() {
        Random random = new Random();
        for (int i = 1; i <= 20; i++) {
            User user = new User();
            user.setUserId("USER" + String.format("%03d", i));
            user.setName("User " + i);
            user.setEmail("user" + i + "@example.com");
            user.setAge(20 + random.nextInt(40));
            user.setPhone("09" + String.format("%08d", random.nextInt(100000000)));
            users.add(user);
        }
    }

    private void loadFromFile() {
        try {
            users = objectMapper.readValue(new File(JSON_FILE_PATH), 
                new TypeReference<List<User>>() {});
            log.info("Loaded {} users from file", users.size());
        } catch (IOException e) {
            log.error("Error loading users from file", e);
            users = new ArrayList<>();
        }
    }

    private void saveToFile() {
        try {
            objectMapper.writeValue(new File(JSON_FILE_PATH), users);
            log.info("Saved {} users to file", users.size());
        } catch (IOException e) {
            log.error("Error saving users to file", e);
        }
    }

    public List<User> getAllUsers() {
        return users;
    }

    public Optional<User> getUserById(String userId) {
        return users.stream()
                .filter(user -> user.getUserId().equals(userId))
                .findFirst();
    }

    public User createUser(User user) {
        if (getUserById(user.getUserId()).isPresent()) {
            throw new IllegalArgumentException("User ID already exists");
        }
        users.add(user);
        saveToFile();
        return user;
    }

    public User updateUser(String userId, User updatedUser) {
        User existingUser = getUserById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setAge(updatedUser.getAge());
        existingUser.setPhone(updatedUser.getPhone());
        
        saveToFile();
        return existingUser;
    }

    public void deleteUser(String userId) {
        users.removeIf(user -> user.getUserId().equals(userId));
        saveToFile();
    }
} 