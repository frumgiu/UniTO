package com.example.demotelegramapi.service;

import com.example.demotelegramapi.model.TelegramContent;
import org.drinkless.tdlib.TdApi;
import java.util.List;

public interface TelegramService {
    boolean readChatsListId(int limit);

    List<TelegramContent> getChatHistory(int limit, long lastRecentMs);

    void joinChatUnito();

    void update(TdApi.AuthorizationState authorizationState);

    void createClient();
}