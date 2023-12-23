package com.bot.categorytree.commands;

import com.bot.categorytree.service.MessageService;
import com.bot.categorytree.util.Emojis;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Класс используется для отправки
 * сообщения об ошибке, если введена
 * некорректная команда
 */

@Service
@RequiredArgsConstructor
public class ErrorCommand implements BotCommand {

    private final MessageService messageService;

    /**
     * Инициализация команды
     * @param update Данные пользователя из тг, десеарлизованные
     *      *               из JSON in JAVA class
     */
    @Override
    public void initCommand(Update update) {
        messageService.sendMessage(update, Emojis.ERROR + "Неизвестная команда!");
    }
}
