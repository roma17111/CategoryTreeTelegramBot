package com.bot.categorytree.commands;

import com.bot.categorytree.controllers.MessageService;
import com.bot.categorytree.util.Emojis;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@RequiredArgsConstructor
public class ErrorCommand implements BotCommand {

    private final MessageService messageService;

    @Override
    public void initCommand(Update update) {
        messageService.sendMessage(update, Emojis.ERROR + "Неизвестная команда!");
    }
}
