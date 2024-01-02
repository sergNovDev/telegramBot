package org.sergn.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sergn.service.ConsumerService;
import org.sergn.service.MainService;
import org.sergn.service.ProducerService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static model.RabbitQueue.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConsumerServiceImpl implements ConsumerService {
    private final MainService mainService;


    @Override
    @RabbitListener(queues = TEXT_MESSAGE_UPDATE)
    public void consumeTextMessageUpdates(Update update) {
        log.debug("NODE: Text message is received");
        mainService.processTextMessage(update);
//        var message = update.getMessage();
//        var sendMessage = new SendMessage();
//        sendMessage.setChatId(message.getChatId().toString());
//        sendMessage.setText("Hello from NODE");
//        producerService.producerAnswer(sendMessage);
    }

    @Override
    @RabbitListener(queues = DOC_MESSAGE_UPDATE)
    public void consumeDocMessageUpdates(Update update) {
        log.debug("NODE: Text message is received");
        mainService.processDocMessage(update);
    }

    @Override
    @RabbitListener(queues = PHOTO_MESSAGE_UPDATE)
    public void consumePhotoMessageUpdates(Update update) {
        log.debug("NODE: Photo message is received");
        mainService.processPhotoMessage(update);
    }
}
