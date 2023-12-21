package com.bot.categorytree.controllers;

import com.bot.categorytree.commands.CommandInitializer;
import com.bot.categorytree.configuration.BotConfig;
import com.bot.categorytree.service.MessageService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class BotListener extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    private final MessageService messageService;
    private final CommandInitializer commandInitializer;

    public BotListener(BotConfig botConfig, MessageService messageService, CommandInitializer commandInitializer) {
        super(botConfig.getBotToken());
        this.botConfig = botConfig;
        this.messageService = messageService;
        this.commandInitializer = commandInitializer;
    }

    @PostConstruct
    public void init() {
        botConfig.registerBot(this);
    }

    @Override
    public void onUpdateReceived(Update update) {
        commandInitializer.check(update);
    }

    @Override
    public String getBotUsername() {
        return botConfig.getUsername();
    }
}
