package utils.model;

import java.util.Date;

public class TelegramContent {
    private long msgId;
    private String message;
    private Date sentDate;
    private int dateOrder;

    public TelegramContent(long msgId, String message, int dateOrder) {
        this.msgId = msgId;
        this.message = message;
        this.dateOrder = dateOrder;
    }

    public TelegramContent() {}

    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

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

    @Override
    public String toString() {
        return "TelegramContent{" +
                "msgId=" + msgId +
                ", message='" + message + '\'' +
                ", sentDate=" + sentDate +
                ", dateOrder=" + dateOrder +
                '}';
    }
}