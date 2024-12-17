package com.example.demo.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

@Controller
@RequestMapping("/register")
public class ResistrationController {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public ResistrationController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@PostMapping
	public String register(User user, @RequestParam("password-confirm") String passwordConfirm, Model model) {
		// パスワード一致確認
		if (!user.getPassword().equals(passwordConfirm)) {
			model.addAttribute("error", "パスワードが一致しません。");
			model.addAttribute("showRegister", true); // 登録画面を表示するフラグ
			return "login";
		}

		// 重複確認
		if (userRepository.findByUsername(user.getUsername()).isPresent()) {
			model.addAttribute("error", "このユーザーネームは既に使用されています。");
			model.addAttribute("showRegister", true); // 登録画面を表示するフラグ
			return "login";
		}

		// パスワードの暗号化
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRole("USER");

		// データベース保存
		userRepository.save(user);
		return "redirect:/login?success";
	}
}
