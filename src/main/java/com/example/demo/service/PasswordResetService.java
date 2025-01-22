package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import jakarta.transaction.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entity.PasswordResetToken;
import com.example.demo.entity.User;
import com.example.demo.repository.PasswordResetTokenRepository;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

	private final UserRepository userRepository;
	private final PasswordResetTokenRepository tokenRepository;
	private final EmailService emailService;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public void requestPasswordReset(String email) {
		if (!userRepository.existsByEmail(email)) {
			return; // セキュリティ上、存在しない場合も成功したように振る舞う
		}

		// 古いトークンを削除
		tokenRepository.deleteByEmail(email);

		// 新しいトークンを生成
		String token = UUID.randomUUID().toString();
		PasswordResetToken resetToken = new PasswordResetToken();
		resetToken.setEmail(email);
		resetToken.setToken(token);
		resetToken.setExpirationDate(LocalDateTime.now().plusHours(1));

		tokenRepository.save(resetToken);

		// メールテンプレートのプレースホルダー設定
		Map<String, String> placeholders = new HashMap<>();
		String resetLink = "http://localhost:8080/password/resetting?token=" + token;
		placeholders.put("resetLink", resetLink);

		// メール送信
		emailService.sendTemplatedEmail(email, "パスワードリセットのご案内", "password-reset", placeholders);
	}

	// トークンの有効性チェック
	public boolean isValidToken(String token) {
		return tokenRepository.findByToken(token)
				.filter(t -> t.getExpirationDate().isAfter(LocalDateTime.now()) && !t.getUsed())
				.isPresent();
	}

	// パスワード更新処理
	public void updatePassword(String token, String newPassword) {
		PasswordResetToken resetToken = tokenRepository.findByToken(token)
				.orElseThrow(() -> new RuntimeException("無効なトークンです"));

		User user = userRepository.findByEmail(resetToken.getEmail())
				.orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));

		user.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(user);

		// トークンを無効化
		resetToken.setUsed(true);
		tokenRepository.save(resetToken);
	}
}
