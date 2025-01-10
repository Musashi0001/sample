package com.example.demo.config;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        org.springframework.security.core.AuthenticationException exception)
            throws IOException, ServletException {

        if ("This account is banned.".equals(exception.getMessage())) {
            response.sendRedirect("/login?error=banned");
        } else {
        	String username = request.getParameter("username"); // 入力されたユーザーネームを取得
            response.sendRedirect("/login?error&username=" + username); // パラメータとして渡す
        }
    }
}
