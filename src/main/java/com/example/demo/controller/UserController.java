package com.example.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserController {
	
	private final UserService userService;
	
	@PostMapping("/delete")
    public String deleteUserAccount(RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        try {
            userService.deleteUserByUsername(username);
            SecurityContextHolder.clearContext();  // セッションをクリアしてログアウト
            redirectAttributes.addFlashAttribute("successMessage", "アカウントが削除されました。");
            return "redirect:/login?logout";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "アカウント削除に失敗しました。");
            return "redirect:/home";
        }
    }
}
