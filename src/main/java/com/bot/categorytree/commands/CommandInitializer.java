package com.bot.categorytree.commands;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CommandInitializer {

    private final Map<String, BotCommand> commands = new HashMap<>();

    private final StartCommand startCommand;
    private final HelpCommand helpCommand;
    private final AddElementCommand addElementCommand;

    @PostConstruct
    public void init() {
        commands.put(BotCommands.START.getName(), startCommand);
        commands.put(BotCommands.HELP.getName(), helpCommand);
        commands.put(BotCommands.ADD_ELEMENT.getName(), addElementCommand);
    }

    public void check(Update update) {
        String text = update.getMessage().getText();
        commands.forEach((key, value) -> {
            if (text.equals(key)|| text.startsWith(key)) {
                value.initCommand(update);
            }
        });
    }
}
