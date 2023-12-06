package com.example.telegrammicroservice.handlers;

import com.example.telegrammicroservice.service.TelegramService;
import org.drinkless.tdlib.Client;
import org.drinkless.tdlib.TdApi;
import org.springframework.beans.factory.annotation.Autowired;

public class AutorizationUpdatehandler implements Client.ResultHandler {

    private final TelegramService service;

    public AutorizationUpdatehandler(@Autowired TelegramService service) {
        this.service = service;
    }

    @Override
    public void onResult(TdApi.Object object) {
        // print("Unsupported update:" + newLine + object);
        if (object.getConstructor() == TdApi.UpdateAuthorizationState.CONSTRUCTOR) {
            System.out.println("UPDATE autorizzazione");
            System.out.println(object);
            service.update(((TdApi.UpdateAuthorizationState) object).authorizationState);
        }
    }
}