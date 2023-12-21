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

    @Query(value = "select * from categories where level_of_nesting =0", nativeQuery = true)
    Category findRoot();

    List<Category> findAllByParrentCategory(Category category);
}
