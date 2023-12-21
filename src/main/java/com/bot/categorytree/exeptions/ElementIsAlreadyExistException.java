package com.bot.categorytree.exeptions;

public class ElementIsAlreadyExistException extends Exception {

    public ElementIsAlreadyExistException() {
    }

    public ElementIsAlreadyExistException(String message) {
        super(message);
    }
}
