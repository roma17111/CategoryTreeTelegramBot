package com.bot.categorytree.commands;

import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Bot commant Interface
 */
public interface BotCommand {

     void initCommand(Update update);
}
