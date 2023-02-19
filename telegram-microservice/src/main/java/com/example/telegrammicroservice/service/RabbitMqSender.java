package com.example.telegrammicroservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import utils.common.TelegramContent;
import java.util.List;

@Service
public class RabbitMqSender {
    private RabbitTemplate rabbitTemplate;
    private static Logger logger = LoggerFactory.getLogger(RabbitMqSender.class);

    public RabbitMqSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.routingkey}")
    private String routingkey;

    public void send(List<TelegramContent> listTelegram) {
        logger.info("RabbitMQ Message sending with data : " + listTelegram);
        rabbitTemplate.convertAndSend(exchange, routingkey, listTelegram);
    }
}