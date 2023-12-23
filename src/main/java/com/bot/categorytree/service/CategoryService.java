package com.bot.categorytree.service;

import com.bot.categorytree.entity.Category;
import com.bot.categorytree.exeptions.CategoryNotFoundException;
import com.bot.categorytree.exeptions.ElementIsAlreadyExistException;
import com.bot.categorytree.exeptions.ParentNotFoundException;
import com.bot.categorytree.exeptions.RemoveElementException;
import com.bot.categorytree.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.*;

/**
 * Основной сервис для работы
 * с логикой дерева категорий
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * Добавить корень к дереву.
     * Если корень уже существует, метод не сработает
     * @param root Имя корня дерева
     * @return result method
     */

    public boolean addRoot(Update update, String root) {
        long chatId = update.getMessage().getChatId();
        long count = categoryRepository.countAllByChatId(chatId);
        if (count > 0) {
            return false;
        }
        Category category = Category.builder()
                .categoryName(root.toLowerCase())
                .chatId(chatId)
                .build();
        categoryRepository.save(category);
        return true;
    }

    /**
     * Добваление дочернего элемента к существующему
     * @param parentEl Имя родительского элемента
     * @param childEl Имя дочерного элемента
     * @param chatId Идентификатор чата пользователя
     * @throws ParentNotFoundException Родительский элемент не
     *                                  найден в базе данных
     * @throws ElementIsAlreadyExistException Если элемент существует,
     *                                      то его второй раз добовить нельзя!
     */
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
            Category category = Category.builder()
                    .chatId(chatId)

                    .categoryName(childEl.toLowerCase())
                    .parrentCategory(parent)
                    .build();
            categoryRepository.save(category);
        }
    }

    /**
     * Удалениу элемента и всех его дочерних элементов
     * @param element Удаляемый элемент
     * @param chatId Идентификатор пользователя бота
     * @throws CategoryNotFoundException Элемент не найден в бозе
     * @throws RemoveElementException Ошибка при удалении элемента на уровне базы данных
     */
    public void removeElement(String element, long chatId) throws CategoryNotFoundException, RemoveElementException {
        Category category = categoryRepository.findByChatIdAndCategoryName(chatId, element);
        if (category == null) {
            throw new CategoryNotFoundException();
        }
        try {
            category.setParrentCategory(null);
            categoryRepository.delete(category);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            throw new RemoveElementException();
        }

    }

    /**
     * Метод достаёт количество категорий из базы одним сислом
     * @param chatId Идентификатор чата юзера
     * @return Количество категорий, включая корень.
     */
    public long countAllTrees(long chatId) {
        return categoryRepository.countAllByChatId(chatId);
    }

    /**
     * Получить коллекцию категорий пользователя по
     * его идентификатору чата бота
     * @param chatId chat id
     * @return Collection of categories to tree
     */
    public List<Category> getTree(long chatId) {
        return categoryRepository.findAllByChatId(chatId);
    }

    /**
     * Метод для получения корневого элемента дерева
     * по идентификатору чата пользователя
     * @param chatId chat id
     * @return root
     */
    public Category getRootByChatId(long chatId) {
        return categoryRepository.findFirstByChatId(chatId);
    }

    /**
     * Получение всех дочерних категорий, по входящей
     * категории
     * без подкатегорий по идентификатору бот юзера
     * @param category Категория
     * @param chatId ИД чата
     * @return Коллекция дочерний
     */
    public List<Category> findAllByParentCategoryAndChatId(Category category, long chatId) {
        return categoryRepository.findAllByParrentCategoryAndChatId(category, chatId);
    }

    /**
     * Уалить все элементы пользователя из базы
     * @param chatId ИД чата бот пользователя
     */
    public void deleteAllByChatId(long chatId) {
        Category root = getRootByChatId(chatId);
        categoryRepository.delete(root);
    }


}
