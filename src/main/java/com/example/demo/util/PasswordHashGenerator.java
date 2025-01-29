//package com.example.demo.util;
//
//import java.util.Scanner;
//
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//public class PasswordHashGenerator {
//	public static void main(String[] args) {
//		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//		Scanner sc = new Scanner(System.in);
//		System.out.println("パスワード入力→");
//		String rawPassword = sc.next();
//		String encodedPassword = encoder.encode(rawPassword);
//
//		System.out.println("ハッシュ化されたパスワード: " + encodedPassword);
//		sc.close();
//	}
//}
