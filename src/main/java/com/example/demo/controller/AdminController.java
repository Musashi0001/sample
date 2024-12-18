package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminController {
	
	private final UserService userService;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
    	List<User> users = userService.getAllUsers();
    	model.addAttribute("users", users);
    	System.out.println(users);
        return "admin/dashboard";
    }
}
