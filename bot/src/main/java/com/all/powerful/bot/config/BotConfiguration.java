package com.all.powerful.bot.config;

import com.all.powerful.bot.MultiBotManager;
import com.all.powerful.bot.domain.BotList;
import com.all.powerful.bot.mapper.BotListMapper;
import com.all.powerful.bot.service.IBotListService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class BotConfiguration {

    @Bean
    public MultiBotManager multiBotManager(BotListMapper botListMapper) {
        MultiBotManager botManager = new MultiBotManager();

        BotList botList = new BotList();
        botList.setStatus("0");
        List<BotList> list = botListMapper.selectBotListList(botList);
        for (BotList bot : list) {
            botManager.registerBot(bot.getBotUserName(), bot.getBotToken());
        }
        return botManager;
    }
}
