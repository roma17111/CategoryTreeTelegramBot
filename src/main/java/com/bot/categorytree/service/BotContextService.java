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


    private BotContext getActualContext(Update update, long chatId) {
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

    private void initDownload(Update update,
                                 long chatId,
                                 boolean status) {
        BotContext context = getActualContext(update, chatId);
        context.setExcelInDownload(status);
        contextRepository.save(context);
    }


}
