package com.example.demotelegramapi.service;

import org.drinkless.tdlib.TdApi;

public interface TelegramService {
    boolean readChatsListId(int limit);

    String getChatHistory(int limit);

    void joinChatUnito();

    void update(TdApi.AuthorizationState authorizationState);

    void createClient();
}