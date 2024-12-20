package com.example.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.example.demo.enums.BanType;

import lombok.Data;
import lombok.ToString;

@Entity
@Data
@Table(name = "bans")
public class Ban {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // BAN対象のユーザー

    @Enumerated(EnumType.STRING)
    private BanType banType; // BANの種類 (SOFT, TEMPORARY, PERMANENT)

    private String reason; // BAN理由
    private LocalDateTime bannedAt; // BAN適用日時
    private LocalDateTime banExpiry; // BAN解除予定日時 (nullの場合、永久BAN)
    private String executedBy; // 実行者 (管理者のIDまたは名前)
}
