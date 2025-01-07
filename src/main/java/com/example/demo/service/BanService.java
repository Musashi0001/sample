package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Ban;
import com.example.demo.entity.User;
import com.example.demo.enums.BanType;
import com.example.demo.repository.BanRepository;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BanService {

	private final BanRepository banRepository;
	private final UserRepository userRepository;
	private final EmailService emailService;

	// userId に基づいて Ban を取得
	public Optional<Ban> getBanInfoIfBanned(Long userId) {
		// ユーザーを取得
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("ユーザーが見つかりません (ID: " + userId + ")"));

		// isBanned チェック
		if (user.isBanned()) {
			// BAN情報を取得
			return banRepository.findByUserId(userId);
		}

		return Optional.empty();
	}

	public Long getActiveBanIdByUserId(Long userId) {
		Optional<Ban> bans = banRepository.findByUserId(userId);
		return bans.stream()
				.filter(ban -> ban.getBanExpiry() == null || ban.getBanExpiry().isAfter(LocalDateTime.now()))
				.findFirst()
				.map(Ban::getId)
				.orElseThrow(() -> new RuntimeException("有効なBANが見つかりません"));
	}

	//BAN適用
	public void applyOrUpdateBan(Long userId, BanType banType, String reason, Integer durationDays, String executedBy) {
		// ユーザーを取得
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));

		// アクティブなBANを検索
		Ban activeBan = banRepository.findByUserId(userId).stream()
				.filter(ban -> ban.getBanExpiry() == null || ban.getBanExpiry().isAfter(LocalDateTime.now()))
				.findFirst()
				.orElse(null);

		if (activeBan != null) {
			// アクティブなBANを更新
			activeBan.setBanType(banType);
			activeBan.setReason(reason);
			activeBan.setBannedAt(LocalDateTime.now());
			activeBan.setDurationDays(durationDays);
			activeBan.setBanExpiry(durationDays != null ? LocalDateTime.now().plusDays(durationDays) : null);
			activeBan.setExecutedBy(executedBy);
			banRepository.save(activeBan);
			System.out.println(activeBan);
			
			// BAN更新メール送信
			Map<String, String> placeholders = createPlaceholders(reason, banType,
					user.getFormattedDate(activeBan.getBannedAt()), durationDays,
					activeBan.getBanExpiry());

			emailService.sendTemplatedEmail(user.getEmail(), "アカウント停止処分の内容が変更されました", "ban-update", placeholders);
		} else {
			// 新規BANを作成
			Ban newBan = new Ban();
			newBan.setUser(user);
			newBan.setBanType(banType);
			newBan.setReason(reason);
			newBan.setBannedAt(LocalDateTime.now());
			newBan.setDurationDays(durationDays);
			newBan.setBanExpiry(durationDays != null ? LocalDateTime.now().plusDays(durationDays) : null);
			newBan.setExecutedBy(executedBy);
			banRepository.save(newBan);
			// BAN適用メール送信
			Map<String, String> placeholders = createPlaceholders(reason, banType,
					user.getFormattedDate(newBan.getBannedAt()), durationDays,
					newBan.getBanExpiry());
			emailService.sendTemplatedEmail(user.getEmail(), "アカウントが停止されました", "ban-apply", placeholders);
		}

		// is_banned フラグを更新
		user.setBanned(true);
		user.setUpdatedAt(LocalDateTime.now());
		userRepository.save(user);
	}

	//BAN解除
	public void liftBan(Long banId) {
		// BANレコードを削除
		Ban ban = banRepository.findById(banId).orElseThrow(() -> new RuntimeException("BANが見つかりません"));
		banRepository.delete(ban);

		// ユーザーのBAN状態を更新
		User user = ban.getUser();
		user.setBanned(false);
		userRepository.save(user);

		// BAN解除メール送信
		Map<String, String> placeholders = Map.of();
		emailService.sendTemplatedEmail(user.getEmail(), "アカウントBAN解除のお知らせ", "ban-lift", placeholders);
	}

	@Scheduled(cron = "0 0 0 * * *")
	public void updateBanStatus() {
		// 期限切れ BAN の is_banned を解除
		List<User> users = userRepository.findAll();
		for (User user : users) {
			boolean isBanned = banRepository.findByUserId(user.getId()).stream()
					.anyMatch(ban -> ban.getBanExpiry() == null || ban.getBanExpiry().isAfter(LocalDateTime.now()));
			user.setBanned(isBanned);
			userRepository.save(user);
		}
	}

	private Map<String, String> createPlaceholders(String reason, BanType banType, String bannedAt,
			Integer durationDays,
			LocalDateTime banExpiry) {
		return Map.of(
				"reason", reason,
				"banType", banType.getLabel(),
				"bannedAt", bannedAt,
				"durationDays", durationDays != null ? durationDays + "日間" : "無期限",
				"banExpiry", banExpiry != null ? new User().getFormattedDate(banExpiry) : "なし");
	}
}
