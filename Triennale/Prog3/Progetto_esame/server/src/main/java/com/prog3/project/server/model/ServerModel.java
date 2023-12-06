package com.prog3.project.server.model;

import com.example.common.Email;
import com.example.common.Message;
import com.example.common.Operation;
import com.prog3.project.server.FileManipulator;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.*;

public class ServerModel {

    private static final String FILE_INBOX = "inbox-";
    private static final String FILE_OUTBOX = "outbox-";
    private static final String IP = "127.0.0.1";

    private ListProperty<Message> messageListProp;
    private final ObservableList<Message> messagesList;

    public ServerModel(){
        messagesList = FXCollections.observableList(new LinkedList<>());
        messageListProp = new SimpleListProperty<>(messagesList);
    }

    public ObservableList<Message> getMessageListProp() {
        return messageListProp.get();
    }

    public ListProperty<Message> messageListPropProperty() {
        return messageListProp;
    }

    public ObservableList<Message> getMessagesListObs() {
        return messagesList;
    }

    public synchronized void addLog(Message newLog){
        messagesList.add(newLog);
    }

    public synchronized Message loadInbox(String clientMail){
        ArrayList<Email> inboxClient = new ArrayList<>();
        inboxClient = FileManipulator.readEmails(FILE_INBOX + clientMail);
        Message message =  new Message(Operation.GET_INBOX_RESPONSE.getType(), Operation.GET_INBOX_RESPONSE.getMsg(),
                IP, clientMail, inboxClient);
        addLog(message);
        return message;
    }

    public synchronized Message loadOutbox(String clientMail){
        ArrayList<Email> outboxClient = new ArrayList<>();
        outboxClient = FileManipulator.readEmails(FILE_OUTBOX + clientMail);
        Message message = new Message(Operation.GET_OUTBOX_RESPONSE.getType(), Operation.GET_OUTBOX_RESPONSE.getMsg(),
                IP, clientMail, outboxClient);
        addLog(message);
        return message;
    }

    public synchronized Message deleteEmail(String clientEmail, Email emailToDelete){
        if (clientEmail.equals(emailToDelete.getSender()) || emailToDelete.getReceivers().contains(clientEmail)) {
            String directory = clientEmail.equals(emailToDelete.getSender()) ? FILE_OUTBOX : FILE_INBOX;
            String directoryName = directory + clientEmail;
            if (FileManipulator.cancelEmail(directoryName, emailToDelete.getId())) {
                addLog(Message.success(IP, emailToDelete.getSender()));
                return Message.success(IP, clientEmail);
            }
        }
        addLog(Message.failure(IP, emailToDelete.getSender()));
        return Message.failure(IP, clientEmail);
    }

    public synchronized Message addEmailToInboxReceiversNoCorrect(Email emailToSend){
    for (String r: emailToSend.getReceivers()) {
        if (FileManipulator.existsDirectory(FILE_INBOX + r)) {
            FileManipulator.writeEmail(emailToSend, FILE_INBOX + r);
        } else {
            addLog(Message.invalidEmail("127.0.0.1", emailToSend.getSender()));
            Email failed = new Email("postmaster@server.com", Collections.singletonList(emailToSend.getSender()),
                    "Errore, utente sconosciuto: " + r, "Impossibile recapitare email a: " + r + ".\nUtente sconosciuto\n" +
                    "Email originale:\n" + emailToSend.getSubject(), System.currentTimeMillis());
            FileManipulator.writeEmail(failed, FILE_INBOX + emailToSend.getSender());
        }
    }
    emailToSend.setIsNew(false);
    FileManipulator.writeEmail(emailToSend, FILE_OUTBOX + emailToSend.getSender());
    addLog(Message.success(IP, emailToSend.getSender()));
    return Message.success(IP, emailToSend.getSender());
    }

    public synchronized Message addEmailToInboxReceivers(Email emailToSend){
        for (String r: emailToSend.getReceivers()) {
            if (!FileManipulator.existsDirectory(FILE_INBOX + r)) {
                addLog(Message.invalidEmail("127.0.0.1", emailToSend.getSender()));
                return Message.invalidEmail("127.0.0.1", emailToSend.getSender());
            }
        }
        for (String r: emailToSend.getReceivers()) {
            FileManipulator.writeEmail(emailToSend, FILE_INBOX + r);
        }
        emailToSend.setIsNew(false);
        FileManipulator.writeEmail(emailToSend, FILE_OUTBOX + emailToSend.getSender());
        addLog(Message.success(IP, emailToSend.getSender()));
        return Message.success(IP, emailToSend.getSender());
    }

    public synchronized Message setReadEmail(String clientName, Email emailToRead) {
        String directory = FILE_INBOX + clientName;
        emailToRead.setIsNew(false);
        FileManipulator.readEmail(directory, emailToRead);
        addLog(Message.success(IP, emailToRead.getSender()));
        return Message.success(IP, clientName);
    }
}