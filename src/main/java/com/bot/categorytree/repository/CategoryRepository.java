package com.bot.categorytree.repository;

import com.bot.categorytree.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAllByChatId(long chatId);
    Category findByChatIdAndCategoryName(long chatId, String categoryName);
    Category findByChatIdAndParentName(long chatId, String parentName);
    long countAllByChatId(long chatId);

    @Transactional
    @Modifying
    @Query(value = "WITH RECURSIVE category_tree(chat_id, id) AS (SELECT chat_id, category_id FROM categories WHERE chat_id = ?1 AND category_id = ?2 " +
            "UNION ALL SELECT c.category_id, c.chat_id FROM categories c JOIN category_tree ct ON c.parent_id = ct.id) " +
            "DELETE FROM categories WHERE category_id IN (SELECT id FROM category_tree)",
            nativeQuery = true)
    void deleteCategoryAndChildsByChatIdAndId(long chatId, Long categoryId);
}
