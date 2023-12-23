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

/**
 * Класс для обработки загрузки excel document
 * с деревом категорий
 */

@Service
@RequiredArgsConstructor
public class DownloadCommand implements BotCommand {

    private final CategoryService categoryService;
    private final MessageService messageService;

    /**
     * Инициализация команды
     *
     * @param update Данные пользователя из тг, десеарлизованные
     *               *               из JSON in JAVA class
     */
    @Override
    public void initCommand(Update update) {
        String file = getFIlePath(update);
        if (file.isEmpty()) {
            messageService.sendMessage(update, Emojis.ERROR + "Ваше дерево категорий пусто");
        } else {
            messageService.sendDocument(update, file, "Вот ваш excel document" + Emojis.SMILE);
        }
    }

    /**
     * Метод для получения пути к готовому excel
     * файлу для прямого делегирования в инит метод и
     * отправке пользователю.
     *
     * @param update Данные пользователя из тг, десеарлизованные
     *               из JSON in JAVA class
     * @return path to excel file
     */
    private String getFIlePath(Update update) {
        ExcelDownloader excelDownloader = new ExcelDownloader(update, categoryService);
        return excelDownloader.getExelFromDB(update);
    }
}
