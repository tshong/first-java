package com.icsc.ai.app.controller;

import com.icsc.ai.app.model.User;
import com.icsc.ai.app.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
public class WebController {
    private final UserService userService;

    public WebController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("newUser", new User());
        return "users";
    }

    @PostMapping("/user/add")
    public String addUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        if (!isValidUserId(user.getUserId())) {
            log.error("Invalid userId format: {}", user.getUserId());
            redirectAttributes.addFlashAttribute("error", 
                "Invalid User ID format. Please use format: I followed by 5 digits (e.g., I12345)");
            return "redirect:/user";
        }
        try {
            userService.createUser(user);
            log.info("Created new user: {}", user.getUserId());
            redirectAttributes.addFlashAttribute("success", "User created successfully");
        } catch (IllegalArgumentException e) {
            log.error("Failed to create user: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", "User ID already exists");
        }
        return "redirect:/user";
    }

    private boolean isValidUserId(String userId) {
        return userId != null && userId.matches("^I\\d{5}$");
    }

    @GetMapping("/user/edit/{userId}")
    public String showEditForm(@PathVariable String userId, Model model) {
        userService.getUserById(userId).ifPresent(user -> model.addAttribute("user", user));
        return "edit";
    }

    @PostMapping("/user/update/{userId}")
    public String updateUser(@PathVariable String userId, @ModelAttribute User user, 
                           RedirectAttributes redirectAttributes) {
        try {
            userService.updateUser(userId, user);
            log.info("Updated user: {}", userId);
            redirectAttributes.addFlashAttribute("success", "User updated successfully");
        } catch (IllegalArgumentException e) {
            log.error("Failed to update user: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Failed to update user: " + e.getMessage());
        }
        return "redirect:/user";
    }

    @GetMapping("/user/delete/{userId}")
    public String deleteUser(@PathVariable String userId, RedirectAttributes redirectAttributes) {
        userService.deleteUser(userId);
        log.info("Deleted user: {}", userId);
        redirectAttributes.addFlashAttribute("success", "User deleted successfully");
        return "redirect:/user";
    }
} 