package com.prog3.project.client.controllers;

import com.example.common.Email;
import com.example.common.Operation;
import com.prog3.project.client.connection.Connection;
import com.prog3.project.client.model.Client;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ListController {

    @FXML
    private ListView<Email> emailList;

    private Client model;
    private Connection connection;
    private boolean firstConnFlag;

    public void init(Client model, Connection connection){
        this.model = model;
        this.connection = connection;
        this.firstConnFlag = false;

        firstConnection();

        ScheduledExecutorService inboxNew = Executors.newSingleThreadScheduledExecutor(
            (Runnable r) -> { Thread t = new Thread(r); t.setDaemon(true); return t; });
        inboxNew.scheduleAtFixedRate(() -> this.connection.communicate(Operation.GET_INBOX_REQUEST, null), 30, 40, TimeUnit.SECONDS);

        // Bind the ListView (emailList) with the model ListProperty (inbox)
        emailList.itemsProperty().bind(this.model.emailsListProperty());
        // Set the custom cell factory on the ListView with EmailListViewCell
        emailList.setCellFactory(emailListView -> new EmailListViewCell());
        emailList.setFixedCellSize(0);

        /*
         * Add a listener to the ListView. When an item is selected from the ListView
         * the property of the model (currentEmail) is set with the new Email selected.
         */
        emailList.getSelectionModel().selectedItemProperty().addListener((obs, oldEmail, newEmail) -> {
            if (newEmail != null ) {
                if (newEmail.isNew()) {
                    Thread executor = new Thread(() -> connection.communicate(Operation.READ_EMAIL, newEmail));
                    executor.start();
                }
            }
            this.model.setCurrentEmail(newEmail);
        });

        /*
         * Add a listener to the ObservableList inboxContent.
         * When the ObservableList change we show an Alert (new message/s)
         */
        this.model.getInboxContent().addListener((ListChangeListener<Email>) change -> {
            if (firstConnFlag) {
                if (change.next()) {
                    if (!change.getAddedSubList().isEmpty()) {
                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Messaggi arrivati");
                            alert.setHeaderText("Ti sono appena arrivate " + change.getAddedSubList().size() + " email da leggere");
                            alert.showAndWait();
                        });
                    }
                }
            }
        });

        /*
         * Add a listener to the property of the model (currentEmail).
         * If the new element is NULL it will clear the selection of the ListView (emailList).
         */
        this.model.getCurrentEmailProperty().addListener((obs, oldEmail, newEmail) -> {
            if (newEmail == null)
                emailList.getSelectionModel().select(null);
        });

        /*
         * Add a listener to the currentSectionProperty.
         * When the property change means that the section (inbox/outbox) is changed.
         */
        this.model.currentSectionProperty().addListener((observableValue, oldSection, newSection) -> {
            this.model.setEmailsList();
            this.model.getCurrentList();
        });
    }



    /**
     * Method that will call the first connection to get the emails
     */
    private void firstConnection() {
        Thread executor = new Thread(() -> {
            boolean result = false;
            while(!result) {
                result = connection.communicate(Operation.GET_INBOX_REQUEST, null);
                result = result && connection.communicate(Operation.GET_OUTBOX_REQUEST, null);
            }
            firstConnFlag = true;
        });
        executor.start();

    }
}