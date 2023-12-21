package com.bot.categorytree.commands;

import com.bot.categorytree.callback.BotCallback;
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

    private final ErrorCommand errorCommand;
    private final StartCommand startCommand;
    private final HelpCommand helpCommand;
    private final AddElementCommand addElementCommand;
    private final RemoveElementCommand removeElementCommand;
    private final ViewTreeCommand viewTreeCommand;

    @PostConstruct
    public void init() {
        commands.put(BotCommands.START.getName(), startCommand);
        commands.put(BotCommands.HELP.getName(), helpCommand);
        commands.put(BotCommands.ADD_ELEMENT.getName(), addElementCommand);
        commands.put(BotCommands.REMOVE_ELEMENT.getName(), removeElementCommand);
        commands.put(BotCommands.VIEW_TREE.getName(), viewTreeCommand);
    }

    public void check(Update update) {
        String text = update.getMessage().getText();
        for (Map.Entry<String, BotCommand> entry : commands.entrySet()) {
            String key = entry.getKey();
            BotCommand value = entry.getValue();
            if (text.equals(key) || text.startsWith(key)) {
                value.initCommand(update);
                return;
            }
        }
        errorCommand.initCommand(update);
    }
}
