package com.bot.categorytree.commands;

import com.bot.categorytree.exeptions.ElementIsAlreadyExistException;
import com.bot.categorytree.exeptions.ParentNotFoundException;
import com.bot.categorytree.service.MessageService;
import com.bot.categorytree.service.CategoryService;
import com.bot.categorytree.util.CommandValidator;
import com.bot.categorytree.util.Emojis;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Класс отвечает за добавление элементов в дерево
 * категорий.
 */

@Service
@RequiredArgsConstructor
@EqualsAndHashCode
public class AddElementCommand implements BotCommand {

    private static final String ERROR_MESSAGE_COMMAND = Emojis.ERROR + "Ошибка " +
            "при добавлении элемента. \n" +
            "Прмер: " + BotCommands.ADD_ELEMENT.getName() +
            "родительский_элемент новый_элемент";

    private final MessageService messageService;
    private final CategoryService categoryService;

    /**
     * Инициализация команды
     *
     * @param update Данные пользователя из тг, десеарлизованные
     *               *               из JSON in JAVA class
     */
    @Override
    public void initCommand(Update update) {
        String[] elements = initValidator(update).getElements();
        int length = elements.length;
        switch (length) {
            case 2 -> {
                addRoot(update);
            }
            case 3 -> {
                addElement(update, elements);
            }
            default -> {
                messageService.sendMessage(update, ERROR_MESSAGE_COMMAND);
            }
        }
    }

    /**
     * Метод для добавленя элемента комманд
     *
     * @param update   Данные пользователя из тг, десеарлизованные
     *                 *      *               из JSON in JAVA class
     * @param elements String parsing to array of elements
     */
    public void addElement(Update update, String[] elements) {
        long chatId = update.getMessage().getChatId();
        String parent = elements[1];
        String child = elements[2];
        try {
            categoryService.addElement(parent, child, chatId);
            messageService.sendMessage(update, Emojis.OK + "Элемент (" + child + ") успешно добавлен.");
        } catch (ParentNotFoundException e) {
            messageService.sendMessage(update, Emojis.ERROR + "Родительский элемент не найден");
            throw new RuntimeException(e);
        } catch (ElementIsAlreadyExistException e) {
            messageService.sendMessage(update, Emojis.ERROR + "Ошибка при добавлении элемент!!! " +
                    "Данный элемент уже существует");
            throw new RuntimeException(e);
        }
    }

    /**
     * Добавления корня к дереву
     *
     * @param update Данные пользователя из тг, десеарлизованные
     *               *      *               из JSON in JAVA class
     */
    private void addRoot(Update update) {
        String root = initValidator(update).getRoot();
        if (root.isEmpty()) {
            messageService.sendMessage(update, ERROR_MESSAGE_COMMAND);
        } else if (!categoryService.addRoot(update, root)) {
            messageService.sendMessage(update, Emojis.ERROR + "Корень уже существует. Удалите текущий корень.");
        } else {
            messageService.sendMessage(update, Emojis.OK + "Корень (" + root + ") добавлен к дереву.");
        }
    }

    /**
     * Инициализация объекта-валидатора
     * @param update Данные пользователя из тг, десеарлизованные
     *      *      *               из JSON in JAVA class
     * @return CommandValidator object
     */
    private CommandValidator initValidator(Update update) {
        String text = update.getMessage().getText();
        return new CommandValidator(text);
    }
}
