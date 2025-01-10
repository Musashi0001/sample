package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

	@GetMapping("/login")
	public String login(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "username", required = false) String username,
			Model model) {
		if ("banned".equals(error)) {
			model.addAttribute("error", "このアカウントはBANされています。ログインできません。");
		} else if (error != null) {
			model.addAttribute("error", "ユーザーネームまたはパスワードが違います。");
		}
		model.addAttribute("username", username); // 入力値を渡す
		return "login";
	}

	@GetMapping("/home")
	public String home(HttpSession session) {
		return "home";
	}
}
