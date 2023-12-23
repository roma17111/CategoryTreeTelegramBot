package com.bot.categorytree.controllers;

import com.bot.categorytree.configuration.BotConfig;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;

/**
 * Класс для отправки
 * сообщений от бота конкретному пользователю
 */

@Service
public  class BotMessageSender extends DefaultAbsSender {

    /**
     * Внедрение бот токена в конструктор
     * @param botConfig Стартовый конфигурационный
     *                  бот класс
     */
    protected BotMessageSender(BotConfig botConfig) {
        super(new DefaultBotOptions(), botConfig.getBotToken());
    }
}
