package com.example.demotelegramapi.handlers;

import org.drinkless.tdlib.Client;
import org.drinkless.tdlib.TdApi;

public class CloseClientHandler implements Client.ResultHandler {
    @Override
    public void onResult(TdApi.Object object) {
        System.out.println("Il client è chiuso -- Ok");
    }
}