package com.bot.categorytree.exeptions;

public class CategoryNotFoundException extends Exception {

    public CategoryNotFoundException() {
    }

    public CategoryNotFoundException(String message) {
        super(message);
    }
}
