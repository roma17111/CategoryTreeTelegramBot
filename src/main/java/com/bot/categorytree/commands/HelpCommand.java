package com.bot.categorytree.commands;

import com.bot.categorytree.controllers.MessageService;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@RequiredArgsConstructor
@EqualsAndHashCode
public class HelpCommand implements BotCommand {

    private static final String TEXT = """
            Команды бота:
                        
            /viewTree Показать дерево категорий в структурированном виде
            /addElement <название элемента> Добавить элемент
            Этот элемент будет корневым,
            если у него нет родителя.
             
            /addElement <родительский элемент> <дочерний элемент>  
            Добавление элемента. 
            /removeElement <название элемента> Удаление элемента
            /download Скачать Excel документ с деревом категорий
            /upload Принимает Excel документ с деревом категорий и сохраняет все элементы в базе данных.             
            """;

    private final static String HELP_PHOTO_PATH = "photos/help.jpg";

    private final MessageService messageService;

    @Override
    public void initCommand(Update update) {
        messageService.sendPhotoAndText(update, TEXT,HELP_PHOTO_PATH);
    }

}
