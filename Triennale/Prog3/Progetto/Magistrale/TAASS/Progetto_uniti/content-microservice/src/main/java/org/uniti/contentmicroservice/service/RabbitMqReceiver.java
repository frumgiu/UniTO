package org.uniti.contentmicroservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utils.common.TelegramContent;
import java.util.List;

@Component
public class RabbitMqReceiver implements RabbitListenerConfigurer {
    @Autowired
    private final TelegramListService telegramListService;
    private final Logger logger = LoggerFactory.getLogger(RabbitMqReceiver.class);

    public RabbitMqReceiver(TelegramListService telegramListService) {
        this.telegramListService = telegramListService;
    }

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar rabbitListenerEndpointRegistrar) {}

    /**
     * @param listTelegram --> lista aggiornata di contenuti Telegram
     * Il servizio resta in ascolto su Rabbit, quando riceve un messaggio con una lista
     * lo setta come lista attuale di contenuti Telegram
     * */
    @RabbitListener(queues = "${spring.rabbitmq.queue}")
    public void receivedMessage(List<TelegramContent> listTelegram) {
        logger.info("Stringa ricevuta.. " + listTelegram);
        telegramListService.setTelegramContentList(listTelegram);
    }
}