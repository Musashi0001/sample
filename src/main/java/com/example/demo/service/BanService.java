package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;

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

	// ユーザーがBAN中かを判定する
	public boolean isUserBanned(Long userId) {
		List<Ban> activeBans = banRepository.findByUserIdAndBanExpiryAfter(userId, LocalDateTime.now());
		return !activeBans.isEmpty();
	}

	public Long getActiveBanIdByUserId(Long userId) {
		List<Ban> bans = banRepository.findByUserId(userId);
		return bans.stream()
				.filter(ban -> ban.getBanExpiry() == null || ban.getBanExpiry().isAfter(LocalDateTime.now()))
				.findFirst()
				.map(Ban::getId)
				.orElseThrow(() -> new RuntimeException("有効なBANが見つかりません"));
	}

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
			activeBan.setBanExpiry(durationDays != null ? LocalDateTime.now().plusDays(durationDays) : null);
			activeBan.setExecutedBy(executedBy);
			banRepository.save(activeBan);
		} else {
			// 新規BANを作成
			Ban newBan = new Ban();
			newBan.setUser(user);
			newBan.setBanType(banType);
			newBan.setReason(reason);
			newBan.setBannedAt(LocalDateTime.now());
			newBan.setBanExpiry(durationDays != null ? LocalDateTime.now().plusDays(durationDays) : null);
			newBan.setExecutedBy(executedBy);
			banRepository.save(newBan);
		}

		// is_banned フラグを更新
		user.setBanned(true);
		userRepository.save(user);
	}

	public void liftBan(Long banId) {
		// BANレコードを削除
		Ban ban = banRepository.findById(banId).orElseThrow(() -> new RuntimeException("BANが見つかりません"));
		banRepository.delete(ban);

		// ユーザーのBAN状態を更新
		User user = ban.getUser();
		user.setBanned(false);
		userRepository.save(user);
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
}
