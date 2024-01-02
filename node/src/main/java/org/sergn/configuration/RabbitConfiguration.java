package org.sergn.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static model.RabbitQueue.*;
import static model.RabbitQueue.ANSWER_MESSAGE;

@Configuration
public class RabbitConfiguration {
    @Bean
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    //В брокере сообщений не нужно будет выполнять ничего, при первом подклбчении очереди будут созданы
    //автоматически

    @Bean
    public Queue textMessageQueue(){
        return new Queue(TEXT_MESSAGE_UPDATE);
    }

    @Bean
    public Queue photoMessageQueue(){
        return new Queue(PHOTO_MESSAGE_UPDATE);
    }

    @Bean
    public Queue docMessageQueue(){
        return new Queue(DOC_MESSAGE_UPDATE);
    }

    @Bean
    public Queue answerMessageQueue(){
        return new Queue(ANSWER_MESSAGE);
    }
}

