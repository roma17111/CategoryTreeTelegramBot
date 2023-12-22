package com.bot.categorytree.service;

import com.bot.categorytree.controllers.BotMessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

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
                .parseMode(ParseMode.HTML)
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

    public void sendMessagePlusKeyboard(Update update,
                                        String text,
                                        InlineKeyboardMarkup markup) {
        long chatId = update.getMessage().getChatId();
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .replyMarkup(markup)
                .parseMode(ParseMode.HTML)
                .build();
        try {
            messageSender.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void editMessagePlusKeyboard(Update update,
                                        String text,
                                        InlineKeyboardMarkup markup) {
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        int messageId = update.getCallbackQuery().getMessage().getMessageId();
        EditMessageText message = EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(text)
                .replyMarkup(markup)
                .parseMode(ParseMode.HTML)
                .build();
        try {
            messageSender.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void editMessage(Update update,
                            String text) {
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        int messageId = update.getCallbackQuery().getMessage().getMessageId();
        EditMessageText message = EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(text)
                .parseMode(ParseMode.HTML)
                .build();
        try {
            messageSender.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendDocument(Update update,
                             String path,
                             String caption) {
        long chatId = update.getMessage().getChatId();
        try (InputStream inputStream = new FileInputStream(path)){
            InputFile inputFile = new InputFile(inputStream, path.substring(path.indexOf("/")));
            SendDocument document = SendDocument.builder()
                    .document(inputFile)
                    .caption(caption)
                    .chatId(chatId)
                    .parseMode(ParseMode.HTML)
                    .build();
            messageSender.execute(document);
        } catch (IOException | TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }

}
