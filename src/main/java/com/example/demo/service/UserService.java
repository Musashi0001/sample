package com.example.demo.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		
	    System.out.println("【デバッグ】データベースから取得したパスワード: " + user.getPassword());

		// Spring Security の UserDetails を返す
		return org.springframework.security.core.userdetails.User.builder()
				.username(user.getUsername())
				.password(user.getPassword())
				 .roles(user.getRole().name()) // ロールを追加
				.build();
	}
	
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}
	
	public long getUsersCount() {
		return userRepository.count();
	}
	
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません (ID: " + id + ")"));
    }
}