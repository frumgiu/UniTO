package com.prog3.project.client.model;

import com.example.common.Email;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Client {

    public static final String INBOX = "INBOX";
    public static final String OUTBOX = "OUTBOX";

    /**
     * clientEmail -> email of the client (example@gmail.it)
     * emails -> ListProperty to visualize all the emails
     * inboxContent -> ObservableList to save the inbox emails
     * outboxContent -> ObservableList to save the outbox emails
     * currentEmails -> current emails selected from the user
     * currentSection -> stringProperty to set the inbox-outbox filteredList
     */
    private StringProperty clientEmail;
    private ListProperty<Email> emailsList;
    private ObservableList<Email> inboxContent;
    private ObservableList<Email> outboxContent;

    private ObjectProperty<Email> currentEmail;
    private StringProperty currentSection;

    /**
     * Constructor
     * @param clientEmailValue email of the client
     */
    public Client(String clientEmailValue) {
        if (clientEmailValue != null) {
            clientEmail = new SimpleStringProperty(clientEmailValue);

            emailsList = new SimpleListProperty<>();
            inboxContent = FXCollections.observableList(new LinkedList<>());
            outboxContent = FXCollections.observableList(new LinkedList<>());

            currentSection = new SimpleStringProperty(INBOX);
            currentEmail = new SimpleObjectProperty<>();
            emailsList.set(inboxContent);
        }
    }

    public StringProperty clientEmailProperty() {
        return clientEmail;
    }

    public String getClientEmailPropertyValue() {
        return clientEmail.getValue();
    }

    public ListProperty<Email> emailsListProperty() {
        return emailsList;
    }

    public ObservableList<Email> getCurrentList() {
        return emailsList.getValue();
    }

    public ObservableList<Email> getInboxContent() {
        return inboxContent;
    }

    public ObjectProperty<Email> getCurrentEmailProperty() {
        return currentEmail;
    }

    public Email getCurrentEmailPropertyValue() {
        return currentEmail.getValue();
    }

    public void setCurrentEmail(Email email) {
        if (email != null)
            email.setIsNew(false);
        Platform.runLater(() -> currentEmail.setValue(email));
    }

    public StringProperty currentSectionProperty() {
        return currentSection;
    }

    public void setEmailsList() {
        switch (currentSection.getValue()) {
            case "INBOX" -> emailsList.set(inboxContent);
            case "OUTBOX" -> emailsList.set(outboxContent);
        }
    }

    /**
     * Method used to set the email received from the server side.
     * @param emailList - List with the emails
     * @param listToAdd - Name of the list where to add emails
     */
    public void loadEmails(List<Email> emailList, String listToAdd) {
        for (Email email: emailList) {
            if (Objects.equals(listToAdd, "INBOX") && !inboxContent.contains(email)) {
                inboxContent.add(email);
                sortEmails(inboxContent);
            } else if (Objects.equals(listToAdd, "OUTBOX") && !outboxContent.contains(email)){
                outboxContent.add(email);
                sortEmails(outboxContent);
            }
        }
        if (Objects.equals(listToAdd, "INBOX"))
            inboxContent.removeIf(email -> !emailList.contains(email));
        else
            outboxContent.removeIf(email -> !emailList.contains(email));
    }

    private void sortEmails(ObservableList<Email> list) {
        list.sort((a, b) -> {
            if (a.getDate() < b.getDate())
                return 1;
            if (a.getDate() > b.getDate())
                return -1;
            return 0;
        });
    }
}