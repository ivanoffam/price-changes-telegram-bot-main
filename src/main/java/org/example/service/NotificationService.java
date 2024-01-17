package org.example.service;

import org.example.controller.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class NotificationService {
    @Autowired
    private TelegramBot telegramBot;

    public void sendSimpleMessage(String chatId, String text) {
        SendMessage message = new SendMessage(chatId, text);
        try {
            telegramBot.execute(message);
        } catch (TelegramApiException e) {
            System.err.println("Telegram API Exception for chatId " + chatId + ": " + e.getMessage());
        }
    }
}