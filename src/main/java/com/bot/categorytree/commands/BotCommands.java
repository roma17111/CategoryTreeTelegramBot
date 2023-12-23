package com.bot.categorytree.commands;

import lombok.Getter;

/**
 * enum Bot Commands constants and values
 */
@Getter
public enum BotCommands {

    START("/start"),
    VIEW_TREE("/viewTree"),
    ADD_ELEMENT("/addElement"),
    REMOVE_ELEMENT("/removeElement"),
    DOWNLOAD("/download"),
    UPLOAD("/upload"),
    HELP("/help");

    private final String name;

    BotCommands(String name) {
        this.name = name;
    }
}
