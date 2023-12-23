package com.bot.categorytree.commands;

import com.bot.categorytree.callback.Callback;
import com.bot.categorytree.service.BotContextService;
import com.bot.categorytree.service.CategoryService;
import com.bot.categorytree.service.MessageService;
import com.bot.categorytree.util.Emojis;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для обработки команды загрузки
 * excel документа пользователем в бот
 * /upload
 */

@Service
@RequiredArgsConstructor
public class UploadBotCommend implements BotCommand, Callback {

    private static final String CANCEL_CALLBACK = "CANCEL_CALLBACK";
    private static final String DOCUMENT_TEXT = Emojis.DOCUMENT + "Прикрепите документ в формате xlsx";
    private static final String CANCEL_TEXT = Emojis.CANCEL + "Я передумал";

    private final MessageService messageService;
    private final BotContextService botContextService;

    /**
     * Инициализация команды
     *
     * @param update Данные пользователя из тг, десеарлизованные
     *               *               из JSON in JAVA class
     */
    @Override
    public void initCommand(Update update) {
        long chatId = update.getMessage().getChatId();
        botContextService.initDownload(chatId, true);
        getLoadKeyboard(update);
    }

    /**
     * Отправка сообщения пользователю
     * о предложении прикрепить готовый документ боту.
     * Есть кнопка для отмены операции, если пользователь передумал
     *
     * @param update Данные пользователя из тг, десеарлизованные
     *               из JSON in JAVA class
     */
    private void getLoadKeyboard(Update update) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        InlineKeyboardButton button = new InlineKeyboardButton(CANCEL_TEXT);
        button.setCallbackData(CANCEL_CALLBACK);
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        rows.add(List.of(button));
        markup.setKeyboard(rows);
        messageService.sendMessagePlusKeyboard(update, DOCUMENT_TEXT, markup);
    }

    /**
     * Метод по обработки логики
     * после нажатии об отмене выгрузки
     * документа в бот
     *
     * @param update Данные пользователя из тг, десеарлизованные
     *               из JSON in JAVA class
     */
    private void cancelUpload(Update update) {
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        botContextService.initDownload(chatId, false);
        messageService.editMessage(update, Emojis.OK + "Выгрузка отменена");
    }

    /**
     * Метод по обработки нажатия отмены загрузки
     * @param update Данные пользователя из тг, десеарлизованные
     *      *               из JSON in JAVA class
     */
    @Override
    public void ifCallback(Update update) {
        String data = update.getCallbackQuery().getData();
        if (data.equals(CANCEL_CALLBACK)) {
            cancelUpload(update);
        }
    }
}
