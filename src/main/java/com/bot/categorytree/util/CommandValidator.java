package com.bot.categorytree.util;

import com.bot.categorytree.commands.BotCommands;
import lombok.Data;

/**
 * Класс для валидации исходящих команд
 * пользователя /addRoot /addElement и т.п
 */

@Data
public class CommandValidator {

    private final String command;

    /**
     * Метод проверяет корректность команды
     * по добавлению элементов в дерево в целом
     * @return результат. Если ресультат false
     * Бот не пропустит команду в обработчик логики
     */

    private boolean isValidCommand() {
        String text = command.trim();
        for (int i = 0; i < text.length(); i++) {
            // В названии категории не может быть цифра)
            if (Character.isDigit(text.charAt(i))) {
                return false;
            }
        }
        // /addElement parent child
        if (!text.contains(" ")) {
            return false;
        }
        int cLen = BotCommands.ADD_ELEMENT.getName().length();
        if (text.charAt(cLen) != ' ') {
            return false;
        }
        return true;
    }

    /**
     * Проверяет корректность ввода комманды
     * /removeElement
     * @return result
     */
    public boolean isValidRemoveCommand() {
        String text = command.trim();
        if (!text.contains(" ")) {
            return false;
        }
        int cLen = BotCommands.REMOVE_ELEMENT.getName().length();
        if (text.charAt(cLen) != ' ') {
            return false;
        }
        return true;
    }

    /**
     * После ввода команды /addRoot root
     * метод возвращает root для дальнейшей
     * удобной работы с корнем в других логических слоях приложения.
     * @return Результать обработки команды
     * Есл введена некорректная комманды, метод вернёт пустую строку.
     */
    public String getRoot() {
        if (!isValidCommand()) {
            return "";
        } else {
            String[] strings = command.trim().split(" ");
            return strings[1].trim();
        }
    }

    /**
     * После ввода команды /removeElement el
     * метод возвращает el для дальнейшей
     * удобной работы с элементом в других логических слоях приложения.
     * @return Результать обработки команды
     * Есл введена некорректная комманды, метод вернёт пустую строку.
     */
    public String getRemoveElement() {
        if (!isValidRemoveCommand()) {
            return "";
        } else {
            return command.trim().split(" ")[1];
        }
    }

    /**
     * Метод пытается распарсить входящие данные
     * Пример /addElement parent child
     * @return new String[]{"/addElement","parent","child"};
     */
    public String[] getElements() {
        if (!isValidCommand()) {
            return new String[0];
        } else {
            return command.trim().split(" ");
        }
    }
}
