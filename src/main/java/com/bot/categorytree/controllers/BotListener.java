package com.bot.categorytree.controllers;

import com.bot.categorytree.botDocuments.DocumentsHandler;
import com.bot.categorytree.callback.BotCallback;
import com.bot.categorytree.commands.CommandInitializer;
import com.bot.categorytree.configuration.BotConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class BotListener extends TelegramLongPollingBot {

    private final BotConfig botConfig;

    private final CommandInitializer commandInitializer;
    private final BotCallback botCallback;
    private final DocumentsHandler documentsHandler;

    public BotListener(BotConfig botConfig, CommandInitializer commandInitializer, BotCallback botCallback, DocumentsHandler documentsHandler) {
        super(botConfig.getBotToken());
        this.botConfig = botConfig;
        this.commandInitializer = commandInitializer;
        this.botCallback = botCallback;
        this.documentsHandler = documentsHandler;
    }

    @PostConstruct
    public void init() {
        botConfig.registerBot(this);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            commandInitializer.check(update);
        } else if (update.hasCallbackQuery()) {
            botCallback.ifCallback(update);
        } else if (update.hasMessage()&& update.getMessage().hasDocument()) {
            documentsHandler.init(update);
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getUsername();
    }
}
