package com.example.demotelegramapi.handlers;

import org.drinkless.tdlib.Client;
import org.drinkless.tdlib.TdApi;
import org.springframework.stereotype.Component;
@Component
public class HistoryReceivedHandler implements Client.ResultHandler {
    @Override
    public void onResult(TdApi.Object object) {
        System.out.println("Ok, funziona!");
        //System.out.println(object.toString());
    }
}