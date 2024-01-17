package org.example.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@Data
public class BotConfig {

    @Value("${crypto.maxUsers}")
    private int maxUsers;

    @Value("${crypto.apiMexcUrl}")
    private String apiMexcUrl;

    @Value("${crypto.priceChangeThreshold}")
    private double priceChangeThreshold;

    @Value("${telegram.botName}")
    private String botName;

    @Value("${telegram.botToken}")
    private String botToken;

    @Value("${telegram.chatId}")
    private String chatId;
}
