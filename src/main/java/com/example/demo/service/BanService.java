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

	// BANを適用する
	public Ban applyBan(Long userId, BanType banType, String reason, Integer durationDays, String executedBy) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));

		Ban ban = new Ban();
		ban.setUser(user);
		ban.setBanType(banType);
		ban.setReason(reason);
		ban.setBannedAt(LocalDateTime.now());
		ban.setExecutedBy(executedBy);

		if (banType == BanType.TEMPORARY) {
			ban.setBanExpiry(LocalDateTime.now().plusDays(durationDays));
		}

		return banRepository.save(ban);
	}

	// BANを解除する
	public void liftBan(Long banId, Long userId) {
		Ban ban = banRepository.findById(banId)
				.orElseThrow(() -> new RuntimeException("BANが見つかりません"));
		banRepository.delete(ban);
		
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));
		user.setBanned(false);
		userRepository.save(user);
	}

	// 自動解除（スケジュールタスク）
	@Scheduled(cron = "0 0 0 * * *") // 毎日深夜0時に実行
	public void autoLiftExpiredBans() {
		List<Ban> expiredBans = banRepository.findByBanExpiryBefore(LocalDateTime.now());
		banRepository.deleteAll(expiredBans);
	}
}
