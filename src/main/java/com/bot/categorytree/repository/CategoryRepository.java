package com.bot.categorytree.repository;

import com.bot.categorytree.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAllByChatId(long chatId);

    Category findByChatIdAndCategoryName(long chatId, String categoryName);

    long countAllByChatId(long chatId);

    long countAllByChatIdAndCategoryName(long chatId, String parentName);

    @Query(value = "select * from categories where level_of_nesting =0 and chat_id = :chat_id", nativeQuery = true)
    Category findRootByChatId(@Param("chat_id") long chatId);

    List<Category> findAllByParrentCategoryAndChatId(Category category , long chatId);

    @Query(value = "select max(level_of_nesting) from categories limit 1", nativeQuery = true)
    int findMaxLevelOfNesting();

    long countAllByParrentCategoryAndChatId(Category parentCategory, long chatId);

    void deleteAllByChatId(long chatId);
}
