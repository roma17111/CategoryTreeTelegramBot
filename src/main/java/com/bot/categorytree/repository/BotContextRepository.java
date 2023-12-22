package com.bot.categorytree.repository;

import com.bot.categorytree.entity.BotContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BotContextRepository extends JpaRepository<BotContext,Long> {

    BotContext findByChatId(long chatId);


}
