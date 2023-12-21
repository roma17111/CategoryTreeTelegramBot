package com.bot.categorytree.commands;

import com.bot.categorytree.service.MessageService;
import com.bot.categorytree.service.CategoryService;
import com.bot.categorytree.util.CommandValidator;
import com.bot.categorytree.util.Emojis;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

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

    @Override
    public void initCommand(Update update) {
        String[] elements = initValidator(update).getElements();
        int length = elements.length;
        switch (length) {
            case 2 -> {
                addRoot(update);
            }
            case 3 -> {
                messageService.sendMessage(update, "В разработке");
            }
            default -> {
                messageService.sendMessage(update, ERROR_MESSAGE_COMMAND);
            }
        }
    }

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

    private CommandValidator initValidator(Update update) {
        String text = update.getMessage().getText();
        return new CommandValidator(text);
    }
}
