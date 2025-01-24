package com.example.demo.config;

import java.time.LocalDateTime;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.example.demo.repository.PasswordResetTokenRepository;

import lombok.RequiredArgsConstructor;

@Component
@Configuration
@RequiredArgsConstructor
public class StartupCleanup {

    private final PasswordResetTokenRepository tokenRepository;

    @Bean
    ApplicationRunner removeExpiredTokensOnStartup() {
        return args -> {
            tokenRepository.deleteExpiredTokens(LocalDateTime.now());
            System.out.println("アプリ起動時に期限切れのトークンを削除しました");
        };
    }
}

