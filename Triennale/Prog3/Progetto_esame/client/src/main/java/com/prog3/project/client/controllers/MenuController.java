package com.prog3.project.client.controllers;

import com.prog3.project.client.connection.Connection;
import com.prog3.project.client.model.Client;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class MenuController {

    @FXML
    public Button btnReload;
    @FXML
    public Label labelReload;
    @FXML
    public Button btnSentLabel;
    @FXML
    public Button btnInboxLabel;
    @FXML
    public VBox menuViewPane;
    @FXML
    public TabPane tabPane;
    @FXML
    public ListView inboxList;
    @FXML
    public ListView outboxList;
    @FXML
    public Button btnNewMessage;
    @FXML
    public Label serverStatusImg;
    @FXML
    public Label serverStatusLabel;
    @FXML
    private Label clientEmail;
    @FXML
    private Label inboxLabel;
    @FXML
    private Label sentLabel;

    private Client model;
    private Connection connection;

    public void init(Client model, Connection connection){
        this.model = model;
        this.connection = connection;
        clientEmail.textProperty().bind(this.model.clientEmailProperty());
        inboxLabel.getStyleClass().add("active-item");

        this.model.currentSectionProperty().addListener((observableValue, oldSection, newSection) -> {
            this.model.setCurrentEmail(null);
            switch (newSection) {
                case Client.INBOX -> {
                    sentLabel.getStyleClass().remove("active-item");
                    inboxLabel.getStyleClass().add("active-item");
                }
                case Client.OUTBOX -> {
                    inboxLabel.getStyleClass().remove("active-item");
                    sentLabel.getStyleClass().add("active-item");
                }
            }
        });

        /*
         * Add listener to the serverConnnectionErrorProperty.
         * When the property change it means that the connection failed.
         */
        this.connection.serverConnectionErrorProperty().addListener((observableValue, oldError, newError) -> {
            if (newError.equals("errore connessione")) {
                Platform.runLater(() -> setStatusServer("server-off", "Stato server - spento"));
            } else if (newError.equals("search connection")) {
                Platform.runLater(() -> setStatusServer("server-search", "Stato server - connettendo"));
            } else {
                Platform.runLater(() -> setStatusServer("server-on", "Stato server - attivo"));
            }
        });
    }

    private void setStatusServer(String style, String label) {
        serverStatusImg.getStyleClass().clear();
        serverStatusImg.getStyleClass().add(style);
        serverStatusLabel.setText(label);
    }

    public void onClickInbox() {
        model.currentSectionProperty().setValue(Client.INBOX);
    }

    public void onClickOutbox() {
        model.currentSectionProperty().setValue(Client.OUTBOX);
    }
}