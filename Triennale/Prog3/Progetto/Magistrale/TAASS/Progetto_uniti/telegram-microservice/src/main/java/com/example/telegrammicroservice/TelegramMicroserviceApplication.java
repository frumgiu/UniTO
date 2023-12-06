package com.example.telegrammicroservice;

import utils.common.TelegramContent;
import com.example.telegrammicroservice.service.TelegramService;
import com.example.telegrammicroservice.service.RabbitMqSender;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class TelegramMicroserviceApplication {

    @Autowired
    private TelegramService telegramService;
    @Autowired
    private final RabbitMqSender rabbitMqSender = new RabbitMqSender(new RabbitTemplate());

    public static void main(String[] args) {
        SpringApplication.run(TelegramMicroserviceApplication.class, args);
    }

    @Bean
    public void run() {
        telegramService.createClient();
        boolean needQuit = false;
        List<TelegramContent> telegramContentList = new ArrayList<>();

        //@TODO Test
//        for (int i = 0; i < 4; i++) {
//            TelegramContent t = new TelegramContent(i+1,"Prova messaggio TG" + i, 0);
//            telegramContentList.add(t);
//        }


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