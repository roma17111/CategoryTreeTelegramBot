package com.bot.categorytree.callback;

import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * callback query Interface
 */


public interface Callback {

    void ifCallback(Update update);
}
