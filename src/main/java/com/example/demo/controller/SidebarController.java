package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SidebarController {
	
	@GetMapping("/profile")
	public String profile() {
		return "profie";
	}
	
	@GetMapping("/settings")
	public String setting() {
		return "settings";
	}
}
