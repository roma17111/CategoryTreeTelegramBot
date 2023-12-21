package com.bot.categorytree.repository;

import com.bot.categorytree.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {


    List<Category> findAllByChatId(long chatId);
    Category findByChatIdAndCategoryName(long chatId, String categoryName);

    long countAllByChatId(long chatId);

    long countAllByChatIdAndCategoryName(long chatId, String parentName);


/*
    @Transactional
    @Modifying
    @Query(value = "WITH RECURSIVE category_tree(chat_id, category_id) AS (SELECT chat_id, category_id FROM categories WHERE chat_id = :chat_id AND category_id = :category_id " +
            "UNION ALL SELECT c.category_id, c.chat_id FROM categories c JOIN category_tree ct ON c.parent_category = ct.category_id) " +
            "DELETE FROM categories WHERE category_id IN (SELECT category_id FROM category_tree)",
            nativeQuery = true)
    void deleteCategoryAndChildsByChatIdAndId(@Param("chat_id") long chatId, @Param("category_id") Long categoryId);*/
}
