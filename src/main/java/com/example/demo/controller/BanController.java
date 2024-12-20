package com.example.demo.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.enums.BanType;
import com.example.demo.service.BanService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class BanController {

	private final BanService banService;

	// BAN適用
	@PostMapping("/ban/{userId}")
    public String applyBan(
            @PathVariable Long userId, // URLパスからユーザーIDを取得
            @RequestParam BanType banType, // フォームからBANの種類を取得
            @RequestParam(required = false) Integer duration, // フォームからBAN期間を取得（任意）
            @RequestParam String reason, // フォームから理由を取得
            RedirectAttributes redirectAttributes // リダイレクト時のメッセージ用
    ) {
		//BAN実行者の名前を取得
		String executedBy = SecurityContextHolder.getContext().getAuthentication().getName();
		
		// BAN適用または更新
	    banService.applyOrUpdateBan(userId, banType, reason, duration, executedBy);
        
        // リダイレクト時にメッセージを渡す
        redirectAttributes.addFlashAttribute("message", "ユーザー " + userId + " に BAN を適用しました。");
        return "redirect:/admin/user/" + userId; // リダイレクト先を指定
    }

	// BAN解除
	@PostMapping("/unban/{userId}")
	public String liftBan(
			@PathVariable Long userId,
			RedirectAttributes redirectAttributes) {
		// userId に基づいて banId を取得
		Long banId = banService.getActiveBanIdByUserId(userId);

		// BAN解除処理
		banService.liftBan(banId);

		// リダイレクト時にメッセージを渡す
		redirectAttributes.addFlashAttribute("message", "ユーザー " + userId + " の BAN を解除しました。");
        return "redirect:/admin/user/" + userId; // リダイレクト先を指定
	}
}
