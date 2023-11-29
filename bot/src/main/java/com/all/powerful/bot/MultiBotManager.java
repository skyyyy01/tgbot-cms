package com.all.powerful.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.HashMap;
import java.util.Map;

public class MultiBotManager {

    private Map<String, TelegramLongPollingBot> bots;

    public MultiBotManager() {
        bots = new HashMap<>();
    }

    public void registerBot(String botUsername, String botToken) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            // Create an instance of your TelegramLongPollingBot
            DynamicTelegramBot bot = new DynamicTelegramBot(botUsername, botToken);

            // Register the bot
            botsApi.registerBot(bot);

            // Store the bot instance in the map
            bots.put(botToken, bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public TelegramLongPollingBot getBot(String botToken) {
        return bots.get(botToken);
    }

    public void closeBot(String botToken) {
        TelegramLongPollingBot bot = bots.get(botToken);
        if (bot != null) {
            bot.onClosing();
            bots.remove(bot.getBotToken());
        }
    }
}
