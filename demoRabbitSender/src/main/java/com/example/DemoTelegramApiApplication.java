package com.example;

import com.example.demotelegramapi.service.RabbitMqSender;
import com.example.demotelegramapi.service.TelegramService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import utils.model.TelegramContent;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class DemoTelegramApiApplication {
    @Autowired
    private TelegramService telegramService;
    @Autowired
    private final RabbitMqSender rabbitMqSender = new RabbitMqSender(new RabbitTemplate());

    public static void main(String[] args) {
        SpringApplication.run(DemoTelegramApiApplication.class, args);
    }

    @Bean
    public void run() {
        telegramService.createClient();
        boolean needQuit = false;
        List<TelegramContent> telegramContentList = new ArrayList<>();
        if (!telegramService.readChatsListId(30))
        {
            System.out.println("Devo essere iscritto al canale per poter accedere alla chat");
            telegramService.joinChatUnito();
        }
        while (!needQuit)
        {
            try {
                System.out.println("*********** FACCIO LA RICHIESTA ***********");
                System.out.println("Lista telegam dimensione prima: " + telegramContentList.size());
                long lastID = telegramContentList.size() == 0 ? 0 : telegramContentList.get(telegramContentList.size()-1).getMsgId();
                telegramContentList.addAll(telegramService.getChatHistory(10, lastID));
                System.out.println("Lista telegam dimensione dopo: " + telegramContentList.size());
                //System.out.println(telegramContentList.get(1).toString());
                rabbitMqSender.send(telegramContentList);
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                needQuit = true;
                e.getStackTrace();
            }
        }
    }
}