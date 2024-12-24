package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Ban;

@Repository
public interface BanRepository extends JpaRepository<Ban, Long> {
	// userId に基づいて BAN を取得
    Optional<Ban> findByUserId(Long userId);
}
