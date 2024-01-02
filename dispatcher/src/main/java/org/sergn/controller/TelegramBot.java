package org.sergn.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@RequiredArgsConstructor
@Slf4j
//Polling означает что бот сам должен постоянно запрашивать
//данные
//Так же потребуется белый IP адрес для работы с WebHook
public class TelegramBot extends /*TelegramLongPollingBot*/ TelegramWebhookBot {

    private final UpdateController updateController;


    @Value("${}")
    private String botUri;

    @PostConstruct
    public void init(){
        updateController.registerBot(this);
        try{
            var setWebhook = SetWebhook.builder()
                    .url(botUri)
                    .build();
            this.setWebhook(setWebhook);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return "misterJavaBot";
    }

    @Override
    public String getBotToken() {
        return "6973903523:AAGf0_Rd7rQoO6XlY3DhdUfPsmozMzrjghY";
    }

   // @Override
    public void onUpdateReceived(Update update) {
        System.out.println("Проверка связи");
        updateController.processUpdate(update);
        System.out.println("Отработали");

//
//        Message message = update.getMessage();
//        System.out.println(message.getText());
//
//
//        Long chatId = update.getMessage().getChatId();
//
//        sendMessage(chatId,"Ответочка");
       // var sendMessage=new SendMessage();
    }

    public void sendAnswerMessage(SendMessage sendMessage){
        try{
            execute(sendMessage);
        }
        catch(TelegramApiException e){
            // log.error("Ошибка отправки сообщения: {}",e);
            System.out.println(e);
        }
    }

    private void sendMessage(Long chatId, String text){
        var chatIdStr=String.valueOf(chatId);
        var sendMessage=new SendMessage(chatIdStr,text);
        try{
            execute(sendMessage);
        }
        catch(TelegramApiException e){
           // log.error("Ошибка отправки сообщения: {}",e);
            System.out.println(e);
        }
    }

    //Методы для вебхука
    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return null; //этот метод нам не понадобится вовсе, так и оставляем
    }

    @Override
    public String getBotPath() {
        return "/updateZ";
    }
}
