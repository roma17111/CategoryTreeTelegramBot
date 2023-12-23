package com.bot.categorytree.exeptions;

/**
 * Ошибка при удалении элемента
 */
public class RemoveElementException extends Exception {

    public RemoveElementException() {
    }

    public RemoveElementException(String message) {
        super(message);
    }
}
