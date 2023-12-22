package com.bot.categorytree.commands;

import com.bot.categorytree.service.CategoryService;
import com.bot.categorytree.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.facilities.filedownloader.TelegramFileDownloader;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

@Service
@RequiredArgsConstructor
public class UploadBotCommend implements BotCommand {

    private final CategoryService categoryService;
    private final MessageService messageService;

    @Override
    public void initCommand(Update update) {
        messageService.sendMessage(update,"Выгрузка началась - заглушка!");
    }

}
