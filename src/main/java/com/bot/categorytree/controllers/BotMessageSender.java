package com.bot.categorytree.controllers;

import com.bot.categorytree.configuration.BotConfig;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;

@Service
public  class BotMessageSender extends DefaultAbsSender {
    protected BotMessageSender(BotConfig botConfig) {
        super(new DefaultBotOptions(), botConfig.getBotToken());
    }
}
