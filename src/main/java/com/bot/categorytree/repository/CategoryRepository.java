package com.bot.categorytree.repository;

import com.bot.categorytree.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAllByChatId(long chatId);

    Category findByChatIdAndCategoryName(long chatId, String categoryName);

    long countAllByChatId(long chatId);

    Category findFirstByChatId(long chatId);

    List<Category> findAllByParrentCategoryAndChatId(Category category , long chatId);

}
