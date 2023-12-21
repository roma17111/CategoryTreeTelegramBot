package com.bot.categorytree.service;

import com.bot.categorytree.entity.Category;
import com.bot.categorytree.exeptions.ElementIsAlreadyExistException;
import com.bot.categorytree.exeptions.ParentNotFoundException;
import com.bot.categorytree.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
                .categoryName(root.toUpperCase())
                .chatId(chatId)
                .levelOfNesting(0)
                .build();
        categoryRepository.save(category);
        return true;
    }

    public void addElement(String parentEl,
                              String childEl,
                              long chatId) throws ParentNotFoundException, ElementIsAlreadyExistException {
        Category parent = categoryRepository.findByChatIdAndCategoryName(chatId, parentEl);
        Category child = categoryRepository.findByChatIdAndCategoryName(chatId, childEl);
        if (parent == null) {
            throw new ParentNotFoundException();
        }
        if (child != null) {
            throw new ElementIsAlreadyExistException();
        } else {
            long levelOfNesting = parent.getLevelOfNesting()+1;
            Category category = Category.builder()
                    .chatId(chatId)
                    .categoryName(childEl.toUpperCase())
                    .parentName(parent.getCategoryName().toUpperCase())
                    .levelOfNesting(levelOfNesting)
                    .parentId(parent.getId())
                    .build();
            categoryRepository.save(category);
        }
    }

}
