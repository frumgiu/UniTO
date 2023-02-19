package org.uniti.contentmicroservice.service;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import utils.common.TelegramContent;
import java.util.ArrayList;
import java.util.List;

@Service
@Scope("singleton")
public class TelegramListService {
    List<TelegramContent> telegramContentList = new ArrayList<>();

    public List<TelegramContent> getTelegramContentList() {
        return telegramContentList;
    }

    public void setTelegramContentList(List<TelegramContent> telegramContentList) {
        this.telegramContentList = telegramContentList;
    }
}