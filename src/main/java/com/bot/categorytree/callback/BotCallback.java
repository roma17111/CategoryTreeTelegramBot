package com.bot.categorytree.callback;

import com.bot.categorytree.commands.UploadBotCommend;
import com.bot.categorytree.entity.Category;
import com.bot.categorytree.service.CategoryService;
import com.bot.categorytree.service.MessageService;
import com.bot.categorytree.util.Emojis;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для обработки основной логики
 * при нажатии пользователем тг бота на кнопку
 */

@Service
@RequiredArgsConstructor
public class BotCallback implements Callback {

    private final CategoryService categoryService;
    private final MessageService messageService;
    private final UploadBotCommend uploadBotCommend;


    /**
     * Метод, отвечающий за отправку сообщения с кнопками
     * или без кнопок после нажатия команды /viewTree.
     * Если дерево пустое, отправляется соответствующее сообщение,
     * что дерево пустое
     *
     * @param update   Данные пользователя из тг, десеарлизованные
     *                 *               из JSON in JAVA class
     * @param category Entity сущность класса категории
     */
    private void setCategoryKeyboard(Update update, Category category) {
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        String element = "";
        boolean isRoot = categoryService.getRootByChatId(chatId).equals(category);
        if (isRoot) {
            element = "Корень";
        } else {
            element = "Родительская категория";
        }
        String text = String.format("Сртруктура дерева:\n\n%s <b>%s</b>", element, category.getCategoryName());
        List<Category> categoryList = categoryService.findAllByParentCategoryAndChatId(category, chatId);
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        if (!categoryList.isEmpty()) {
            categoryList.forEach(el -> {
                InlineKeyboardButton button = new InlineKeyboardButton(Emojis.BRANCH + " " + el.getCategoryName());
                button.setCallbackData(el.getCallback());
                rows.add(List.of(button));
            });
        }
        if (!isRoot) {
            InlineKeyboardButton back = new InlineKeyboardButton(Emojis.BACK + "Назад");
            back.setCallbackData(category.getBackCallback());
            rows.add(List.of(back));
        }
        markup.setKeyboard(rows);
        messageService.editMessagePlusKeyboard(update, text, markup);
    }


    public void getCategoryKeyboard(Update update) {
        long chatId = update.getMessage().getChatId();
        Category category = categoryService.getRootByChatId(chatId);
        if (category == null) {
            getNullableTree(update);
            return;
        }
        String text = String.format("Сртруктура дерева:\n\nКорень <b>%s</b>", category.getCategoryName());
        List<Category> categoryList = categoryService.findAllByParentCategoryAndChatId(category, chatId);
        if (categoryList == null || categoryList.isEmpty()) {
            messageService.sendMessage(update, text);
            return;
        }
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        categoryList.forEach(el -> {
            InlineKeyboardButton button = new InlineKeyboardButton(Emojis.BRANCH + " " + el.getCategoryName());
            button.setCallbackData(el.getCallback());
            rows.add(List.of(button));
        });
        markup.setKeyboard(rows);
        messageService.sendMessagePlusKeyboard(update, text, markup);
    }


    /**
     * Основная логика обработки нажатий на кнопки пользователем
     * @param update Данные пользователя из тг, десеарлизованные
     *               *               из JSON in JAVA class
     */
    @Override
    public void ifCallback(Update update) {
        String data = update.getCallbackQuery().getData();
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        categoryService.getTree(chatId).forEach(el -> {
            if (data.equals(el.getCallback())) {
                setCategoryKeyboard(update, el);
            } else if (data.equals(el.getBackCallback())) {
                setCategoryKeyboard(update, el.getParrentCategory());
            }
        });
        if (data == null) {
            messageService.editMessage(update, "тык-тык");
        }
        uploadBotCommend.ifCallback(update);
    }

    private void getNullableTree(Update update) {
        messageService.sendMessage(update, Emojis.ERROR + " Дерево пустое");
    }


}
