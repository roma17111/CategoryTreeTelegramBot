package com.bot.categorytree.exeptions;

/**
 * Родительский элемент не найден
 */
public class ParentNotFoundException extends Exception {

    public ParentNotFoundException() {
    }

    public ParentNotFoundException(String message) {
        super(message);
    }
}
