package org.sergn.service;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateProducer {

    void produce(String rabbitQueue, Update update);
}
