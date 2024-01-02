package org.sergn.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sergn.service.UpdateProducer;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@Slf4j
@RequiredArgsConstructor
public class UpdateProducerImpl implements UpdateProducer {
    private final RabbitTemplate rabbitTemplate;
    @Override
    public void produce(String rabbitQueue, Update update) {
        log.debug(update.getMessage().getText());
        //Автоматически будет вызвана наша конвертация созданная в конфигурации
        rabbitTemplate.convertAndSend(rabbitQueue,update);
    }
}
