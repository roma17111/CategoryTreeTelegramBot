package com.bot.categorytree.exeptions;

/**
 * Элемент уже существует в базе
 */
public class ElementIsAlreadyExistException extends Exception {

    public ElementIsAlreadyExistException() {
    }

    public ElementIsAlreadyExistException(String message) {
        super(message);
    }
}
