package com.example.common;

import org.apache.commons.lang3.ObjectUtils;

import java.io.Serial;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Email implements Serializable {

    public final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");

    @Serial
    private static final long serialVersionUID = 5950469529316463575L;
    private UUID id;
    private String sender;
    private List<String> receivers;
    private String subject;
    private String emailText;
    private long date;
    private boolean isNew;

    /**
     * Constructor n.1
     * @param sender - Sender of the Email
     * @param receivers - Receivers of the Email
     * @param subject - Subject of the Email
     * @param emailText - Text of the Email
     * @param date - Date of the Email (no time)
     */
    public Email(String sender, List<String> receivers, String subject,
                 String emailText, long date) {
        if (ObjectUtils.allNotNull(sender,receivers,subject,emailText,date)) {
            id = UUID.randomUUID();
            this.sender = sender;
            this.receivers = receivers;
            this.subject = subject;
            this.emailText = emailText;
            this.date = date;
            isNew = false;
        }
    }

    public Email() {
        id = UUID.randomUUID();
        isNew = false;
    }

    public UUID getId() {
        return id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public List<String> getReceivers() {
        return receivers;
    }

    public void setReceivers(List<String> receivers) {
        this.receivers = receivers;
    }

    /**
     * @return a new String with all the receivers address without the characters '[' and ']'
     */
    public String receiversToString() {
        String strRec = this.receivers.toString();
        return strRec.replace("[","").replace("]","");
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getEmailText() {
        return emailText;
    }

    public void setEmailText(String emailText) {
        this.emailText = emailText;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }

    /**
     * @param client - email address of the email client (example@example.com)
     *               (No need to have it in the new email receivers)
     * @param receiver - email address who sent the email that will be replayed.
     * @return a new String with all the receivers of the new email, without the client.
     */
    public String getReceiversNewEmail(String client, String receiver) {
        StringBuilder recStr = new StringBuilder(receiver).append(" ");
        for (String address : this.receivers) {
            if (!address.equals(client))
                recStr.append(address).append(" ");
        }
        return recStr.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && this.getClass() == obj.getClass()) {
            Email email = (Email) obj;
            return this.id.equals(email.id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sender, receivers, subject, emailText, date);
    }

    @Override
    public String toString() {
        return "Email ID : " + id;
    }
}