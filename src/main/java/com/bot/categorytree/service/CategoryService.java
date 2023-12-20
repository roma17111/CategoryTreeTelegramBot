package com.bot.categorytree.service;

import com.bot.categorytree.entity.Category;
import com.bot.categorytree.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;


    public boolean addRoot(Update update, String root) {
        long chatId = update.getMessage().getChatId();
        long count = categoryRepository.countAllByChatId(chatId);
        if (count > 0) {
            return false;
        }
        Category category = Category.builder()
                .categoryName(root)
                .chatId(chatId)
                .levelOfNesting(0)
                .build();
        categoryRepository.save(category);
        return true;
    }
}
