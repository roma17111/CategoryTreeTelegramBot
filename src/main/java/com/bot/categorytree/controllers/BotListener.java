package com.bot.categorytree.controllers;

import com.bot.categorytree.configuration.BotConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class BotListener extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    private final MessageService messageService;

    public BotListener(BotConfig botConfig, MessageService messageService) {
        super(botConfig.getBotToken());
        this.botConfig = botConfig;
        this.messageService = messageService;
    }

    @PostConstruct
    public void init() {
        botConfig.registerBot(this);
    }

    @Override
    public void onUpdateReceived(Update update) {
        SendMessage message = new SendMessage();
        message.setText("test");
        message.setChatId(update.getMessage().getChatId());
        try {
            messageService.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getUsername();
    }
}
