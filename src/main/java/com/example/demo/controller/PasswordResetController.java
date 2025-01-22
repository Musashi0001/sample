package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.service.PasswordResetService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/password")
@RequiredArgsConstructor
public class PasswordResetController {
	
    private final PasswordResetService resetService;
	
	@GetMapping("/reset")
	public String passwordReset() {
		return "password-reset";
	}
	
	@PostMapping("/reset")
	public String requestPasswordReset(@RequestParam String email, Model model) {
        resetService.requestPasswordReset(email);
        model.addAttribute("message", "リセットリンクを送信しました。") ;
        return "password-reset";
	}
	
	@GetMapping("/resetting")
	public String showResetForm(@RequestParam("token") String token, Model model) {
        if (!resetService.isValidToken(token)) {
            model.addAttribute("error", "無効または期限切れのトークンです。");
            return "password-resetting-error";
        }
        model.addAttribute("token", token);
        return "password-resetting";
    }
	
    @PostMapping("/resetting")
    public String resetPassword(@RequestParam("token") String token,
                                @RequestParam("password") String password,
                                Model model) {
        if (!resetService.isValidToken(token)) {
            model.addAttribute("error", "無効または期限切れのトークンです。");
            return "password-resetting-error";
        }

        resetService.updatePassword(token, password);
        return "login";
    }
}
