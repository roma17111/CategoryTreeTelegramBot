package com.bot.categorytree.botDocuments;

import com.bot.categorytree.configuration.BotConfig;
import com.bot.categorytree.excel.ExcelUploader;
import com.bot.categorytree.exeptions.ElementIsAlreadyExistException;
import com.bot.categorytree.exeptions.InvalidExcelException;
import com.bot.categorytree.exeptions.ParentNotFoundException;
import com.bot.categorytree.service.BotContextService;
import com.bot.categorytree.service.CategoryService;
import com.bot.categorytree.service.MessageService;
import com.bot.categorytree.util.Emojis;
import javassist.bytecode.analysis.Executor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Данный класс служит для
 * обработки документов, присланных
 * прльзователем в телеграмм бот
 */

@Service
@RequiredArgsConstructor
public class DocumentsHandler {

    private final BotContextService contextService;
    private final MessageService messageService;
    private final CategoryService categoryService;
    private final BotConfig botConfig;

    /**
     * Основной входной метод для
     * обработки входных документов
     * @param update Класс библиотеки telegram bots
     *               для обработки входящих данных от пользователя
     */

    public void init(Update update) {
        long chatId = update.getMessage().getChatId();
        if (contextService.inStatusDownloadExcel(update)) {
            try {
                File file = getFileFromBot(update);
                upload(file, update);
                Files.delete(Path.of(file.getPath()));
            } catch (IOException |
                     ExecutionException |
                     InterruptedException |
                     InvalidExcelException |
                     ElementIsAlreadyExistException |
                     ParentNotFoundException |
                     InvalidFormatException e) {
                messageService.sendMessage(update, Emojis.ERROR + "Ошибка при выгрузке документа!!!");
            } finally {
                contextService.initDownload(chatId, false);
            }
            messageService.sendMessage(update, "Загрузка успешна. Телеграм вас не забудет.");
            contextService.initDownload(chatId, false);
        }
    }

    /**
     * Метод для инициализации загрузки
     * документов в тг бот
     * @param file Файл документа
     * @param update Класс - обработчик данных из телеграмм
     * @throws ExecutionException Обработка Ошибок ExecuterService
     * @throws InterruptedException Обработка ошибок при выполнении потока
     * @throws InvalidExcelException Обработка ошибок при
     *                               некорректно выгруженном документе пользователем
     */

    private void upload(File file,Update update) throws ExecutionException, InterruptedException, InvalidExcelException, ElementIsAlreadyExistException, ParentNotFoundException, IOException, InvalidFormatException {

        ExcelUploader uploader = new ExcelUploader(categoryService, file,update);
        boolean result = uploader.upload(file);
        if (!result) {
            throw new InvalidExcelException();
        }
    }

    /**
     * Преобразование отправленного файла пользователем
     * в обычный файл для дальнейшего проброса в специальный класс
     * @param update Данные пользователя из тг, десеарлизованные
     *               из JSON in JAVA class
     * @return обычный файл из стандартной библиотеки java
     * @throws IOException Обработка ошибок потоков ввода-вывода
     */
    private File getFileFromBot(Update update) throws IOException {
        GetFile getFile = new GetFile(update.getMessage().getDocument().getFileId());
        var file = messageService.executeFile(getFile);
        return messageService.download(file.getFilePath());
    }
}
