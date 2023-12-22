package com.bot.categorytree.util;

import com.bot.categorytree.commands.BotCommands;
import lombok.Data;

@Data
public class CommandValidator {

    private final String command;

    private boolean isValidCommand() {
        String text = command.trim();
        for (int i = 0; i < text.length(); i++) {
            if (Character.isDigit(text.charAt(i))) {
                return false;
            }
        }
        if (!text.contains(" ")) {
            return false;
        }
        int cLen = BotCommands.ADD_ELEMENT.getName().length();
        if (text.charAt(cLen) != ' ') {
            return false;
        }
        return true;
    }

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

    public String getRoot() {
        if (!isValidCommand()) {
            return "";
        } else {
            String[] strings = command.trim().split(" ");
            return strings[1].trim();
        }
    }

    public String getRemoveElement() {
        if (!isValidRemoveCommand()) {
            return "";
        } else {
            return command.trim().split(" ")[1];
        }
    }

    public String[] getElements() {
        if (!isValidCommand()) {
            return new String[0];
        } else {
            return command.trim().split(" ");
        }
    }
}
