package com.example.telegrammicroservice.service;

import org.drinkless.tdlib.TdApi;
import utils.common.TelegramContent;

import java.util.List;

public interface TelegramService {
    boolean readChatsListId(int limit);

    List<TelegramContent> getChatHistory(int limit, long lastRecentMs);

    void joinChatUnito();

    void update(TdApi.AuthorizationState authorizationState);

    void createClient();
}