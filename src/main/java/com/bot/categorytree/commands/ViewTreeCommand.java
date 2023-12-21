package com.bot.categorytree.commands;

import com.bot.categorytree.callback.BotCallback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@RequiredArgsConstructor
public class ViewTreeCommand implements BotCommand{

    private final BotCallback botCallback;

    @Override
    public void initCommand(Update update) {
        botCallback.getCategoryKeyboard(update);
    }
}
