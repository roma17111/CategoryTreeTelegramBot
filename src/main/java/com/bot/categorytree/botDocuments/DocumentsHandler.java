package com.bot.categorytree.botDocuments;

import com.bot.categorytree.configuration.BotConfig;
import com.bot.categorytree.excel.ExcelUploader;
import com.bot.categorytree.exeptions.InvalidExcelException;
import com.bot.categorytree.service.BotContextService;
import com.bot.categorytree.service.CategoryService;
import com.bot.categorytree.service.MessageService;
import com.bot.categorytree.util.Emojis;
import javassist.bytecode.analysis.Executor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
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

@Service
@RequiredArgsConstructor
public class DocumentsHandler {

    private final BotContextService contextService;
    private final MessageService messageService;
    private final CategoryService categoryService;
    private final BotConfig botConfig;

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
                     InvalidExcelException e) {
                messageService.sendMessage(update, Emojis.ERROR + "Ошибка при выгрузке документа!!!");
                throw new RuntimeException(e);
            } finally {
                contextService.initDownload(chatId, false);
            }
            messageService.sendMessage(update, "Загрузка успешна. Телеграм вас не забудет.");
            contextService.initDownload(chatId, false);
        }
    }

    private void upload(File file,Update update) throws ExecutionException, InterruptedException, InvalidExcelException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        ExcelUploader uploader = new ExcelUploader(categoryService, file,update);
        boolean result = executorService.submit(uploader).get();
        if (!result) {
            throw new InvalidExcelException();
        }
    }

    private File getFileFromBot(Update update) throws IOException {
        GetFile getFileMethod = new GetFile(update.getMessage().getDocument().getFileId());
        File f = File.createTempFile("document", "doc.xlsx", new File("documents"));
        org.telegram.telegrambots.meta.api.objects.File file = messageService.executeFile(getFileMethod);
        String filePath = file.getFilePath();
        URL url = new URL("https://api.telegram.org/file/bot" + botConfig.getBotToken() + "/" + filePath);
        try (InputStream inputStream = url.openStream()) {
            IOUtils.copy(inputStream, new FileOutputStream(f));
        }
        return f;
    }
}
