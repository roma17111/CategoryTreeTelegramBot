package com.bot.categorytree.service;

import com.bot.categorytree.controllers.BotMessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileInputStream;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class MessageService {

    private final BotMessageSender messageSender;

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
            messageSender.execute(sendMessage);
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
            messageSender.execute(photo);
        } catch (IOException | TelegramApiException e) {
            throw new RuntimeException("Error to send photo");
        }
    }


}
