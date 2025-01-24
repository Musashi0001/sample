package com.example.demo.service;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.demo.repository.PasswordResetTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenCleanupService {

    private final PasswordResetTokenRepository tokenRepository;

    // 毎日18時に削除（cron式で設定）
    @Scheduled(cron = "0 0 18 * * ?")
    public void cleanExpiredTokens() {
        tokenRepository.deleteExpiredTokens(LocalDateTime.now());
        System.out.println("毎日18時に期限切れのトークンを削除しました");
    }
}