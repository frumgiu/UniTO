package com.example.demorabbitlistener.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.stereotype.Component;
import utils.model.TelegramContent;
import java.util.List;

@Component
public class RabbitMqReceiver implements RabbitListenerConfigurer {
    private final Logger logger = LoggerFactory.getLogger(RabbitMqReceiver.class);

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar rabbitListenerEndpointRegistrar) {}

    @RabbitListener(queues = "${spring.rabbitmq.queue}")
    public void receivedMessage(List<TelegramContent> listTelegram) {
        logger.info("Stringa ricevuta.. " + listTelegram);
        //System.out.println("Stringa ricevuta: " + listTelegram.toString());
    }
}