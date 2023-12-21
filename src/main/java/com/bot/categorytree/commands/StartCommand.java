package com.bot.categorytree.commands;

import com.bot.categorytree.service.MessageService;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
@EqualsAndHashCode
public class StartCommand implements BotCommand {

    public static final String PATH_TO_START_PHOTO = "photos/tree.jpg";

    private final MessageService messageService;

    @Override
    public void initCommand(Update update) {
        String text = getStartText(update);
        messageService.sendPhotoAndText(update, text, PATH_TO_START_PHOTO);
    }

    private String getStartText(Update update) {
        String name = messageService.getFirstname(update);
        return String.format("""
                Здравствуйте %s!
                                
                Данный телеграмм бот позволяет пользователям
                создавать, просматривать и удалять дерево категорий.
                Список команд: /help""", name);
    }

}
