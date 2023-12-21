package com.bot.categorytree.commands;

import com.bot.categorytree.exeptions.CategoryNotFoundException;
import com.bot.categorytree.exeptions.RemoveElementException;
import com.bot.categorytree.service.CategoryService;
import com.bot.categorytree.service.MessageService;
import com.bot.categorytree.util.CommandValidator;
import com.bot.categorytree.util.Emojis;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@RequiredArgsConstructor
public class RemoveElementCommand implements BotCommand {

    private final CategoryService categoryService;
    private final MessageService messageService;

    private static final String ERROR_MESSAGE = Emojis.ERROR + "Ошибка при удалении элемента!\n" +
            "Пример: /removeElement <b>название элемента<?b>";

    @Override
    public void initCommand(Update update) {
        long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        CommandValidator commandValidator = new CommandValidator(text);
        String el = commandValidator.getRemoveElement();
        System.out.println(el);
        if (el.isEmpty()) {
            messageService.sendMessage(update, ERROR_MESSAGE);
        } else {
            try {
                categoryService.removeElement(el.trim(), chatId);
                messageService.sendMessage(update, Emojis.OK + "Категория и все её дочерние категории " +
                        "успешно удалены.");
            } catch (CategoryNotFoundException e) {
                messageService.sendMessage(update, Emojis.ERROR + "Категория не найдена!!!");
                throw new RuntimeException(e);
            } catch (RemoveElementException e) {
                messageService.sendMessage(update, Emojis.ERROR + "Ошибка!!! Не удалось удалить элемент.");
                throw new RuntimeException(e);
            }
        }
    }
}
