package com.bot.categorytree.callback;

import com.bot.categorytree.entity.Category;
import com.bot.categorytree.service.CategoryService;
import com.bot.categorytree.service.MessageService;
import com.bot.categorytree.util.Emojis;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BotCallback {

    private final CategoryService categoryService;
    private final MessageService messageService;


    private void setCategoryKeyboard(Update update, Category category) {
        String element = "";
        long levelNesting = category.getLevelOfNesting();
        if (levelNesting == 0) {
            element = "Корень";
        } else {
            element = "Родительская категория";
        }
        String text = String.format("Сртруктура дерева:\n\n%s <b>%s</b>", element, category.getCategoryName());
        List<Category> categoryList = categoryService.findAllByParentCategory(category);
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        if (!categoryList.isEmpty()) {
            categoryList.forEach(el -> {
                InlineKeyboardButton button = new InlineKeyboardButton(Emojis.BRANCH + " " + el.getCategoryName());
                button.setCallbackData(el.getCallback());
                rows.add(List.of(button));
            });
        }
        if (levelNesting > 0) {
            InlineKeyboardButton back = new InlineKeyboardButton(Emojis.BACK + "Назад");
            back.setCallbackData(category.getBackCallback());
            rows.add(List.of(back));
        }
        markup.setKeyboard(rows);
        messageService.editMessagePlusKeyboard(update, text, markup);
    }

    public void getCategoryKeyboard(Update update) {
        Category category = categoryService.getRoot();
        if (category == null) {
            getNullableTree(update);
            return;
        }
        String text = String.format("Сртруктура дерева:\n\nКорень <b>%s</b>", category.getCategoryName());
        List<Category> categoryList = categoryService.findAllByParentCategory(category);
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


    public void ifCallback(Update update) {
        String data = update.getCallbackQuery().getData();
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        categoryService.getTree(chatId).forEach(el -> {
            if (data.equals(el.getCallback())) {
                System.out.println(el);
                setCategoryKeyboard(update, el);
            } else if (data.equals(el.getBackCallback())) {
                setCategoryKeyboard(update, el.getParrentCategory());
            }
        });
        if (data == null) {
            messageService.editMessage(update,"тык-тык");
        }
    }

    private void getNullableTree(Update update) {
        messageService.sendMessage(update, Emojis.ERROR + " Дерево пустое");
    }


}
