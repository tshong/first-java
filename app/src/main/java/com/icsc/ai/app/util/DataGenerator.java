package com.icsc.ai.app.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icsc.ai.app.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
public class DataGenerator implements CommandLineRunner {
    private static final String[] FIRST_NAMES = {"James", "Mary", "John", "Patricia", "Robert", "Jennifer", "Michael", "Linda", "William", "Elizabeth"};
    private static final String[] LAST_NAMES = {"Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez"};
    
    @Override
    public void run(String... args) throws Exception {
        File file = new File("users.json");
        if (!file.exists()) {
            List<User> users = generateUsers();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(file, users);
            log.info("Generated {} users and saved to users.json", users.size());
        } else {
            log.info("users.json already exists, skipping generation");
        }
    }

    private List<User> generateUsers() {
        List<User> users = new ArrayList<>();
        Random random = new Random();

        for (int i = 1; i <= 20; i++) {
            User user = new User();
            String firstName = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
            String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
            
            user.setUserId("USER" + String.format("%03d", i));
            user.setName(firstName + " " + lastName);
            user.setEmail(firstName.toLowerCase() + "." + lastName.toLowerCase() + "@example.com");
            user.setAge(20 + random.nextInt(40));
            user.setPhone("09" + String.format("%08d", random.nextInt(100000000)));
            
            users.add(user);
            log.debug("Generated user: {}", user);
        }
        
        return users;
    }
} 