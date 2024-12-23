package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
	
	private final UserService userService;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
    	long usersCount = userService.getUsersCount();
    	List<User> users = userService.getAllUsers();
    	model.addAttribute("usersCount", usersCount);
    	model.addAttribute("users", users);
        return "admin/dashboard";
    }
    
    @GetMapping("/user/{id}")
    public String userDetails(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "admin/user-details";
    }
}