package com.example.demo.config;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		// ユーザーのロールをチェックして遷移先を決定
		for (GrantedAuthority authority : authentication.getAuthorities()) {
			System.out.println(authority.getAuthority());
			if (authority.getAuthority().equals("ROLE_ADMIN")) {
				response.sendRedirect("/admin/dashboard");
				return;
			}
		}

		// ROLE_ADMIN 以外は一般ユーザー用のページへ遷移
		System.out.println("一般ゆうざあ");
		response.sendRedirect("/home");
	}
}
