package com.bot.categorytree.service;

import com.bot.categorytree.entity.BotContext;
import com.bot.categorytree.repository.BotContextRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@RequiredArgsConstructor
public class BotContextService {

    private final BotContextRepository contextRepository;

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

    public void initDownload(long chatId,
                              boolean status) {
        BotContext context = getActualContext(chatId);
        context.setExcelInDownload(status);
        contextRepository.save(context);
    }

    public boolean inStatusDownloadExcel(Update update) {
        long chatId = update.getMessage().getChatId();
        BotContext context = getActualContext(chatId);
        return context.isExcelInDownload();
    }


}
