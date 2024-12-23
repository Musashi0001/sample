package com.example.demo.service;

import java.io.IOException;
import java.util.Map;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateService templateService;

    /**
     * 汎用メール送信
     * @param to 送信先メールアドレス
     * @param subject メール件名
     * @param templateName テンプレートファイル名（拡張子なし）
     * @param placeholders テンプレートに埋め込むデータ
     */
    public void sendTemplatedEmail(String to, String subject, String templateName, Map<String, String> placeholders) {
        try {
            String templateContent = templateService.loadTemplate(templateName);
            String body = templateService.replacePlaceholders(templateContent, placeholders);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true); // HTML形式を有効にする

            mailSender.send(mimeMessage);
        } catch (IOException e) {
            throw new RuntimeException("テンプレート読み込み中にエラーが発生しました", e);
        } catch (MessagingException e) {
            throw new RuntimeException("メール送信中にエラーが発生しました", e);
        }
    }
}
