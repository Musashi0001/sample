package com.example.demo.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class TemplateService {

    public String loadTemplate(String templateName) throws IOException {
        String filePath = "src/main/resources/templates/mail-text/" + templateName + ".html";
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }

    public String replacePlaceholders(String template, Map<String, String> placeholders) {
        String result = template;
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            result = result.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }
        return result;
    }
}
