package com.bot.categorytree.commands;

import com.bot.categorytree.exeptions.CategoryNotFoundException;
import com.bot.categorytree.exeptions.RemoveElementException;
import com.bot.categorytree.service.CategoryService;
import com.bot.categorytree.service.MessageService;
import com.bot.categorytree.util.CommandValidator;
import com.bot.categorytree.util.Emojis;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Класс для обработки команды
 * удаления элементов
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class RemoveElementCommand implements BotCommand {

    private final CategoryService categoryService;
    private final MessageService messageService;

    private static final String ERROR_MESSAGE = Emojis.ERROR + "Ошибка при удалении элемента!\n" +
            "Пример: /removeElement <b>название элемента</b>";

    /**
     * Инициализация команды
     * @param update Данные пользователя из тг, десеарлизованные
     *      *               из JSON in JAVA class
     */
    @Override
    public void initCommand(Update update) {
        long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        CommandValidator commandValidator = new CommandValidator(text);
        String el = commandValidator.getRemoveElement();
        if (el.isEmpty()) {
            messageService.sendMessage(update, ERROR_MESSAGE);
        } else {
            try {
                categoryService.removeElement(el.trim(), chatId);
                messageService.sendMessage(update, Emojis.OK + "Категория и все её дочерние категории " +
                        "успешно удалены.");
            } catch (CategoryNotFoundException e) {
                log.error(e.getMessage(),e);
                messageService.sendMessage(update, Emojis.ERROR + "Категория не найдена!!!");
            } catch (RemoveElementException e) {
                log.error(e.getMessage(),e);
                messageService.sendMessage(update, Emojis.ERROR + "Ошибка!!! Не удалось удалить элемент.");
            }
        }
    }
}
