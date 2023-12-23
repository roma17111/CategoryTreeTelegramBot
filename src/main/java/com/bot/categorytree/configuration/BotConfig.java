package com.bot.categorytree.configuration;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * Стартовые настройки тг бота
 */

@Configuration
@Getter
@Slf4j
public class BotConfig {

    @Value("${bot.username}")
    private String username;

    @Value("${bot.token}")
    private String botToken;

    /**
     * Регистрация тг бота для взаимодействия
     * с telegram API
     * @param bot СЛужебный класс для взаимодействия бота
     *            с пользователем из библиотеки telegrambots
     */
    public void registerBot(TelegramLongPollingBot bot) {
        try {
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(bot);
        } catch (TelegramApiException e) {
            log.error(e.getMessage(),e);
        }
    }

}
