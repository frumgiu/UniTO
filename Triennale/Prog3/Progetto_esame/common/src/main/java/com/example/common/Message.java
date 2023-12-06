package com.example.common;

import org.apache.commons.lang3.ObjectUtils;
import java.io.Serial;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Message implements Serializable {

    public final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");

    @Serial
    private static final long serialVersionUID = 5950169519310163575L;
    private final int type;
    private final String description;
    private String receiver;
    private String clientName;
    private ArrayList<Email> emailsList;

    /**
     * Constructor for server log
     * @param type of log (00 - 09)
     * @param desc of the log message
     */
    public Message(int type, String desc) {
        this.type = type;
        this.description = desc;
    }

    /**
     * Constructor for client server communication without sending new email
     * @param type of communication (10 - 20)
     * @param desc of the communication
     */
    public Message(int type, String desc, String clientName) {
        this.type = type;
        this.description = desc;
        this.clientName = clientName;
    }

    public Message(int type, String desc, String sender, String receiver, ArrayList<Email> emailsList) {
        this.type = type;
        this.description = desc;
        this.clientName = sender;
        this.receiver = receiver;
        this.emailsList = emailsList;
    }

    public int getType(){
        return type;
    }

    public String getDescription(){
        return description;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getClientName() {
        return clientName;
    }

    public ArrayList<Email> getEmailsList() {
        return emailsList;
    }

    public boolean isValid(){
        return ObjectUtils.allNotNull(getDescription(), getType());
    }

    public static Message success(String sender, String receiver) {
        return new Message(Operation.SERVER_RESPONSE_SUCCESS.getType(), Operation.SERVER_RESPONSE_SUCCESS.getMsg(),
                sender, receiver, null);
    }

    public static Message failure(String sender, String receiver) {
        return new Message(Operation.SERVER_RESPONSE_FAILURE.getType(), Operation.SERVER_RESPONSE_FAILURE.getMsg(),
                sender, receiver, null);
    }

    public static Message invalidEmail(String sender, String receiver) {
        return new Message(Operation.SERVER_RESPONSE_INVALID_EMAIL.getType(), Operation.SERVER_RESPONSE_INVALID_EMAIL.getMsg(),
                sender, receiver, null);
    }
}