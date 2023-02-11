package com.example.demotelegramapi.model;

import java.util.Date;

public class TelegramContent {
    private String message;
    private Date sentDate;

    private int dateOrder;

    public TelegramContent(String message, Date sentDate, int dateOrder) {
        this.message = message;
        this.sentDate = sentDate;
        this.dateOrder = dateOrder;
    }

    public TelegramContent() {}

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    public int getDateOrder() {
        return dateOrder;
    }

    public void setDateOrder(int dateOrder) {
        this.dateOrder = dateOrder;
    }
}