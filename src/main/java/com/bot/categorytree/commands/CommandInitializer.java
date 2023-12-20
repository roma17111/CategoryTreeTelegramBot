package com.bot.categorytree.commands;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CommandInitializer {

    private final Map<String, BotCommand> commands = new HashMap<>();

    private final StartCommand startCommand;
    private final HelpCommand helpCommand;

    @PostConstruct
    public void init() {
        commands.put(BotCommands.START.getName(), startCommand);
        commands.put(BotCommands.HELP.getName(), helpCommand);
    }

    public void check(Update update) {
        String text = update.getMessage().getText();
        commands.forEach((key, value) -> {
            if (text.equals(key)) {
                value.initCommand(update);
            }
        });
    }
}
