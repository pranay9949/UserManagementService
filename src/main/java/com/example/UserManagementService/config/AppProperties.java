package com.example.UserManagementService.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@EnableConfigurationProperties
@ConfigurationProperties(prefix = "userapi")
@Configuration
public class AppProperties {
    Map<String,String> messages = new HashMap<>();

    public Map<String, String> getMessages() {
        return messages;
    }

    public void setMessages(Map<String, String> messages) {
        this.messages = messages;
    }
}
