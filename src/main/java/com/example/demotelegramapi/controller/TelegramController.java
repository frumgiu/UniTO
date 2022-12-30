package com.example.demotelegramapi.controller;

import com.example.demotelegramapi.configuration.ConfigurationData;
import com.example.demotelegramapi.service.TelegramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/telegram")
public class TelegramController {
    @Autowired
    private TelegramService telegramService;
    @Autowired
    private ConfigurationData configurationData;

    @GetMapping("/chatsList")
    public String getChatsListId() {
        if (telegramService.readChatsListId(50)) {
            return "Okay";
        } else {
            return "Please, you must join Avvisi UniTo";
        }
    }

    /*
     * Metodo per leggere i messaggi dal canale Avvisi UniTO.
     * Il limite di messaggi da recuperare viene passato come parametro
     * dal client
     */
    @GetMapping("/history")
    public String getMessagesHistoryDue() {
        return telegramService.getChatHistory(2);
    }

    /*
    * Metodo per iscriversi al canale Avvisi UniTO.
    * Bisogna essere iscritti per poter accedere ai messaggi
    */
    @PostMapping("/joinChat")
    public void joinChatUnito() {
        telegramService.joinChatUnito();
    }

    @GetMapping("/chatId")
    public long getChatId() {
        return configurationData.getChatId();
    }
    @GetMapping("/chatName")
    public String getChatName() {
        return "Avvisi UniTO";
    }
}