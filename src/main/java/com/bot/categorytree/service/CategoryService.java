package com.bot.categorytree.service;

import com.bot.categorytree.entity.Category;
import com.bot.categorytree.exeptions.CategoryNotFoundException;
import com.bot.categorytree.exeptions.ElementIsAlreadyExistException;
import com.bot.categorytree.exeptions.ParentNotFoundException;
import com.bot.categorytree.exeptions.RemoveElementException;
import com.bot.categorytree.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.*;

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
                .categoryName(root.toLowerCase())
                .chatId(chatId)
                .callback(UUID.randomUUID().toString())
                .levelOfNesting(0)
                .build();
        categoryRepository.save(category);
        return true;
    }

    @Transactional
    public void addElement(String parentEl,
                           String childEl,
                           long chatId) throws ParentNotFoundException, ElementIsAlreadyExistException {
        Category parent = categoryRepository.findByChatIdAndCategoryName(chatId, parentEl.toLowerCase());
        Category child = categoryRepository.findByChatIdAndCategoryName(chatId, childEl.toLowerCase());
        if (parent == null) {
            throw new ParentNotFoundException();
        }
        if (child != null) {
            throw new ElementIsAlreadyExistException();
        } else {
            long levelOfNesting = parent.getLevelOfNesting() + 1;
            Category category = Category.builder()
                    .chatId(chatId)
                    .callback(UUID.randomUUID().toString())
                    .categoryName(childEl.toLowerCase())
                    .parrentCategory(parent)
                    .levelOfNesting(levelOfNesting)
                    .build();
            categoryRepository.save(category);
        }
    }

    public void removeElement(String element, long chatId) throws CategoryNotFoundException, RemoveElementException {
        Category category = categoryRepository.findByChatIdAndCategoryName(chatId, element);
        System.out.println(category);
        if (category == null) {
            throw new CategoryNotFoundException();
        }
        try {
            category.setParrentCategory(null);
            categoryRepository.save(category);
            categoryRepository.delete(category);
        } catch (Exception e) {
            throw new RemoveElementException();
        }

    }

    public long countAllTrees(long chatId) {
        return categoryRepository.countAllByChatId(chatId);
    }

    public List<Category> getTree(long chatId) {
        return categoryRepository.findAllByChatId(chatId);
    }


}
