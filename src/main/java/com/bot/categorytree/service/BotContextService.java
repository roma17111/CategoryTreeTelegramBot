package com.bot.categorytree.service;

import com.bot.categorytree.entity.BotContext;
import com.bot.categorytree.repository.BotContextRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Сервис для работы с логикой
 * контекста тг бота
 */

@Service
@RequiredArgsConstructor
public class BotContextService {

    private final BotContextRepository contextRepository;

    /**
     * Получения актуального контекста
     * пользователя. Если такого контекста нет в базе данных,
     * создастся новый
     * @param chatId chat id
     * @return Бот контекст
     */

    private BotContext getActualContext(long chatId) {
        BotContext botContext = contextRepository.findByChatId(chatId);
        if (botContext == null) {
            botContext = BotContext.builder()
                    .chatId(chatId)
                    .excelInDownload(false)
                    .build();
            contextRepository.save(botContext);
        }
        return botContext;
    }

    /**
     * Перещёлкивание статуса загрузки
     * контекста выгрузки документа
     * @param chatId chat id
     * @param status Статус о готовности боту
     *               принимать от пользователя excel document
     */
    public void initDownload(long chatId,
                              boolean status) {
        BotContext context = getActualContext(chatId);
        context.setExcelInDownload(status);
        contextRepository.save(context);
    }

    /**
     * Метод проверяет, готов ли сейчас бот
     * принимать документ от пользователя
     * @param update Update reciever from user
     * @return Статус о готовности принимать документ ботом
     */
    public boolean inStatusDownloadExcel(Update update) {
        long chatId = update.getMessage().getChatId();
        BotContext context = getActualContext(chatId);
        return context.isExcelInDownload();
    }


}
