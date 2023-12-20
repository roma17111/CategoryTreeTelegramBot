package com.bot.categorytree.controllers;

import com.bot.categorytree.configuration.BotConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileInputStream;
import java.io.IOException;

@Component
public class MessageService extends DefaultAbsSender {

    public MessageService(BotConfig config) {
        super(new DefaultBotOptions(), config.getBotToken());
    }

    public String getFirstname(Update update) {
        String name = update.getMessage().getChat().getFirstName();
        if (name == null || name.isEmpty()) {
            return "Аноним";
        } else {
            return name;
        }
    }

    public void sendMessage(Update update, String text) {
        long chatId = update.getMessage().getChatId();
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendPhotoAndText(Update update, String text,
                                 String url) {
        long chatId = update.getMessage().getChatId();
        try (FileInputStream fileInputStream = new FileInputStream(url)) {
            InputFile file = new InputFile(fileInputStream, "photo.jpeg");
            SendPhoto photo = SendPhoto.builder()
                    .photo(file)
                    .chatId(chatId)
                    .caption(text)
                    .build();
            execute(photo);
        } catch (IOException | TelegramApiException e) {
            throw new RuntimeException("Error to send photo");
        }
    }

}
