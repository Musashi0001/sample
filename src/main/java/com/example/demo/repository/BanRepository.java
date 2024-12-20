package com.example.demo.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Ban;
import com.example.demo.enums.BanType;

@Repository
public interface BanRepository extends JpaRepository<Ban, Long> {
	// userId に基づいて BAN を取得
    List<Ban> findByUserId(Long userId);
	
    // BAN中のユーザーを取得
    List<Ban> findByUserIdAndBanExpiryAfter(Long userId, LocalDateTime now);

    // 解除予定日時が過ぎたBANを取得
    List<Ban> findByBanExpiryBefore(LocalDateTime now);

    // 特定の種類のBANを取得
    List<Ban> findByBanType(BanType banType);
}
