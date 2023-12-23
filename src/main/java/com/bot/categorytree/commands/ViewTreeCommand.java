package com.bot.categorytree.commands;

import com.bot.categorytree.callback.BotCallback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Обработка команды по
 * предоставлению пользователю дерева категорий
 * в структурированном виде.
 */

@Service
@RequiredArgsConstructor
public class ViewTreeCommand implements BotCommand{

    private final BotCallback botCallback;

    /**
     *  Инициализация команды
     * @param update Данные пользователя из тг, десеарлизованные
     *      *               из JSON in JAVA class
     */
    @Override
    public void initCommand(Update update) {
        botCallback.getCategoryKeyboard(update);
    }
}
