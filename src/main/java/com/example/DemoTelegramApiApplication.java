package com.example;

import com.example.demotelegramapi.service.TelegramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoTelegramApiApplication {
    @Autowired
    private TelegramService telegramService;

    public static void main(String[] args) {
        SpringApplication.run(DemoTelegramApiApplication.class, args);
    }

    @Bean
    public void run() {
        telegramService.createClient();
        boolean needQuit = false;
        if (!telegramService.readChatsListId(30))
        {
            System.out.println("Devo essere iscritto al canale per poter accedere alla chat");
            telegramService.joinChatUnito();
        }
        while (!needQuit)
        {
            try {
                System.out.println("*********** FACCIO LA RICHIESTA ***********");
                System.out.println(telegramService.getChatHistory(6));
                Thread.sleep(10000);
                needQuit = true;
            } catch (InterruptedException e) {
                needQuit = true;
                e.getStackTrace();
            }

        }
    }
}