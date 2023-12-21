package com.bot.categorytree.exeptions;

public class ParentNotFoundException extends Exception {

    public ParentNotFoundException() {
    }

    public ParentNotFoundException(String message) {
        super(message);
    }
}
