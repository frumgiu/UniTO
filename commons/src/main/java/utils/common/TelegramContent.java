package utils.common;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TelegramContent {
    private long msgId;
    private String message;
    private Date sentDate;
    private int dateOrder;

    public TelegramContent(long msgId, String message, int dateOrder) {
        Pattern pattern = Pattern.compile("\"([^\"]+)\"");
        this.msgId = msgId;
        this.dateOrder = dateOrder;
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            this.message = matcher.group(1);
        }
        else {
            this.message = message;
        }
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