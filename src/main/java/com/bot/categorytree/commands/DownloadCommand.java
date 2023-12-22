package com.bot.categorytree.commands;

import com.bot.categorytree.service.CategoryService;
import com.bot.categorytree.excel.ExcelDownloader;
import com.bot.categorytree.service.MessageService;
import com.bot.categorytree.util.Emojis;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
@RequiredArgsConstructor
public class DownloadCommand implements BotCommand {

    private final CategoryService categoryService;
    private final MessageService messageService;

    @Override
    public void initCommand(Update update) {
        String file = getFIlePath(update);
        if (file.isEmpty()) {
            messageService.sendMessage(update, Emojis.ERROR + "Ваше дерево категорий пусто");
        } else {
            messageService.sendDocument(update, file, "Вот ваш excel document" + Emojis.SMILE);
        }
    }

    private String getFIlePath(Update update) {
        String result = "";
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        ExcelDownloader excelDownloader = new ExcelDownloader(update, categoryService);
        Future<String> future = executorService.submit(excelDownloader);
        try {
            result = future.get();
            System.out.println(result);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        } finally {
            executorService.shutdown();
        }
        return result;
    }
}
