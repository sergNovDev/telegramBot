package org.sergn.service.impl;

import lombok.RequiredArgsConstructor;
import org.sergn.controller.UpdateController;
import org.sergn.service.AnswerConsumer;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static model.RabbitQueue.ANSWER_MESSAGE;

@Service
@RequiredArgsConstructor
public class AnswerConsumerImpl implements AnswerConsumer {

    private final UpdateController updateController;

    @Override
    @RabbitListener(queues = ANSWER_MESSAGE)
    public void consume(SendMessage sendMessage) {
        updateController.setView(sendMessage);
    }
}
