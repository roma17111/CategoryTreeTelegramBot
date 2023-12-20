package com.bot.categorytree.controllers;

import com.bot.categorytree.configuration.BotConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;

@Component
public class MessageService extends DefaultAbsSender {

    private final BotConfig config;
    public MessageService(BotConfig config) {
        super(new DefaultBotOptions(), config.getBotToken());
        this.config = config;
    }


}
