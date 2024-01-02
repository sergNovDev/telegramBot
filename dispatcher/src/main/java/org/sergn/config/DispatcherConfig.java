package org.sergn.config;

import org.sergn.controller.TelegramBot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class DispatcherConfig {

    @Bean
    public String start(){
        return "Starting";
    }
    @Bean
    public TelegramBotsApi telegramBotsApi(TelegramBot myBot) throws TelegramApiException {
        var api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(myBot);
        return api;
    }
}