package org.example.service;

import org.example.configuration.BotConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@Lazy
public class MessageHandlerService {
    @Autowired
    private UserManager userManager;
    @Autowired
    private BotConfig config;

    public String handleCommand(long userId, String command) {
        switch (command) {
            case "/start":
                return handleStartCommand(userId);
            case "/restart":
                return handleRestartCommand(userId);
            case "/stop":
                return handleStopCommand(userId);
            default:
                return "Invalid command. Please use /start, /restart or /stop.";
        }
    }

    private String handleStartCommand(long userId) {
        boolean isMaxUsersReached = userManager.getUserCount() >= config.getMaxUsers();

        if (isMaxUsersReached) {
            return "Sorry, the maximum number of users has been reached. Try again later.";
        } else {
            startCommand(userId);
            return "Welcome! You will now receive price change notifications.";
        }
    }

    private void startCommand(long userId) {
        userManager.addUser(userId);
    }

    private String handleRestartCommand(long userId) {
        startCommand(userId);
        stopCommand(userId);
        return "You have resumed receiving notifications about price changes.";
    }

    private String handleStopCommand(long userId) {
        stopCommand(userId);
        return "You have unsubscribed from price change notifications.";
    }

    private void stopCommand(long userId) {
        userManager.removeUser(userId);
    }
}
