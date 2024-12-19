package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/register")
public class ResistrationController {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@PostMapping
	public String register(User user, @RequestParam("password-confirm") String passwordConfirm, Model model,
			RedirectAttributes redirectAttributes) {

		List<String> errors = new ArrayList<>();

		// 重複確認
		if (userRepository.findByUsername(user.getUsername()).isPresent()) {
			errors.add("このユーザーネームは既に使用されています。");
		}
		if (userRepository.findByEmail(user.getEmail()).isPresent()) {
			errors.add("このメールアドレスは既に使用されています。");
		} 
		// パスワード一致確認
		if (!user.getPassword().equals(passwordConfirm)) {
			errors.add("パスワードが一致しません。");
		}

		if (!errors.isEmpty()) {
			model.addAttribute("showRegister", true);
			model.addAttribute("registerErrors", errors);
			model.addAttribute("registerForm", user); // 登録フォーム用データ
			return "login";
		}

		// パスワードの暗号化と保存
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userRepository.save(user);

		redirectAttributes.addFlashAttribute("success", "登録が完了しました。");
		return "redirect:/login";
	}
}
