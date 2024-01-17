package org.example.controller;

import org.example.configuration.BotConfig;
import org.example.service.MessageHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class TelegramBot extends TelegramLongPollingBot {
    @Autowired
    private BotConfig config;
    @Autowired
    private MessageHandlerService messageHandlerService;
    private String chatId = "";

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();

            chatId = message.getChatId().toString();
            long userId = message.getFrom().getId();
            String command = message.getText();

            String text = messageHandlerService.handleCommand(userId, command);
            sendMessage(chatId, text);
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getBotToken();
    }

    private void sendMessage(String chatId, String text) {
        SendMessage message = new SendMessage(chatId, text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.err.println("Telegram API Exception for chatId " + chatId + ": " + e.getMessage());
        }
    }
}
