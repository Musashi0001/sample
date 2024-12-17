package com.example.demo.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {
	public static void main(String[] args) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String rawPassword = "password"; // 任意のパスワード
		String encodedPassword = encoder.encode(rawPassword);

		System.out.println("ハッシュ化されたパスワード: " + encodedPassword);
	}
}
