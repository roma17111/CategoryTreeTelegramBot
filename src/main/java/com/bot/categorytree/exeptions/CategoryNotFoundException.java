package com.bot.categorytree.exeptions;

/**
 * Категория не найдена в базе
 */

public class CategoryNotFoundException extends Exception {

    public CategoryNotFoundException() {
    }

    public CategoryNotFoundException(String message) {
        super(message);
    }
}
