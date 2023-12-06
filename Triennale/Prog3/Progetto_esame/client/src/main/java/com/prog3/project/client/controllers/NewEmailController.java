package com.prog3.project.client.controllers;

import com.example.common.Email;
import com.prog3.project.client.Regex;
import com.prog3.project.client.connection.Connection;
import com.example.common.Operation;
import com.prog3.project.client.model.Client;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class NewEmailController {

    private static final String SPLITTER = " ";
    @FXML
    private VBox newEmailPane;
    @FXML
    private Label toLabel;
    @FXML
    private Label fromLabel;
    @FXML
    private Label subjectLabel;
    @FXML
    private TextField toField;
    @FXML
    private TextField fromField;
    @FXML
    private TextField subjectField;
    @FXML
    private TextArea emailTextArea;
    @FXML
    private Button sendButton;

    private Connection connection;
    private Operation operation;
    private Client model;
    private Stage stage;
    private Email newEmail;

    public void init(Client model, Connection connection, Stage stage, Operation operation) {
        this.connection = connection;
        this.stage = stage;
        this.newEmail = new Email();
        this.operation = operation;
        this.model = model;

        sendButton.setTooltip(setTooltip());

        this.connection.serverConnectionErrorProperty().addListener((observableValue, oldError, newError) -> {
            if (newError.equals("errore connessione")) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Il server non risponde");
                    alert.setContentText("Il server non risponde. " +
                            "Impossibile continuare l'operazione, riprovare piu' tardi.");
                    alert.setHeaderText("Errore connessione");
                    alert.showAndWait();
                });
            }
        });


        this.connection.ServerInvalidUserErrorProperty().addListener((observableValue, oldError, newError) -> {
            if (newError.equals("invalid user")) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Impossibile inviare l'email");
                    alert.setContentText("L'utente non si trova nel sistema, quindi non e' possibile inviare l'email.");
                    alert.setHeaderText("Utente invalido");
                    alert.showAndWait();
                });
            }
        });

        fromField.textProperty().bind(model.clientEmailProperty());
        toField.textProperty().addListener((o, oldValue, newValue) -> {
            String [] tmp = newValue.split(SPLITTER);
            List<String> receivers = parseReceiversString(newValue);
            if (receivers.size() != tmp.length || receivers.contains(this.model.getClientEmailPropertyValue()))
                sendButton.disableProperty().setValue(true);
            else {
                sendButton.disableProperty().setValue(false);
                newEmail.setReceivers(receivers);
            }
        });

        if (this.operation != Operation.SEND_NEW_EMAIL) {
            switch (this.operation) {
                case REPLY_EMAIL -> {
                    toField.textProperty().setValue(model.getCurrentEmailPropertyValue().getSender());
                    subjectField.textProperty().setValue("Re: " + model.getCurrentEmailPropertyValue().getSubject());
                    newEmail.setReceivers(parseReceiversString(toField.textProperty().getValue()));
                }
                case REPLY_ALL_EMAIL -> {
                    toField.textProperty().setValue(model.getCurrentEmailPropertyValue().getReceiversNewEmail(
                            model.getClientEmailPropertyValue(), model.getCurrentEmailPropertyValue().getSender()));
                    subjectField.textProperty().setValue("Re: " + model.getCurrentEmailPropertyValue().getSubject());
                    newEmail.setReceivers(parseReceiversString(toField.textProperty().getValue()));
                }
                case FORWARD_MESSAGE -> {
                    emailTextArea.textProperty().setValue(model.getCurrentEmailPropertyValue().getEmailText());
                    subjectField.textProperty().setValue("Fw: " + model.getCurrentEmailPropertyValue().getSubject());
                    sendButton.disableProperty().setValue(true);
                }
            }
        }
        else
            this.sendButton.disableProperty().setValue(true);
    }

    public void sendEmail() {
        newEmail.setSender(model.getClientEmailPropertyValue());
        newEmail.setEmailText(fromField.textProperty().getValue());
        newEmail.setEmailText(emailTextArea.textProperty().getValue());
        newEmail.setDate(new Date().getTime());
        newEmail.setIsNew(true);
        if (subjectField.textProperty().getValue().equals(""))
            newEmail.setSubject("Oggetto vuoto");
        else
            newEmail.setSubject(subjectField.textProperty().getValue());

        Thread executor = new Thread(() -> {
            if (connection.communicate(operation, newEmail) && connection.ServerInvalidUserErrorProperty().getValue().equals("")) {
                Platform.runLater(() -> stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST)));
            }
           /* else {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Il server non risponde");
                    alert.setContentText("Il server non risponde. " +
                            "Impossibile continuare l'operazione, riprovare piu' tardi.");
                    alert.setHeaderText("Errore connessione");
                    alert.showAndWait();
                } );
            }*/
        });
        executor.start();
    }

    private static Tooltip setTooltip() {
        Tooltip tooltip = new Tooltip("Invia");
        tooltip.setStyle("-fx-font-size: 14");
        tooltip.setShowDelay(Duration.millis(500));
        return tooltip;
    }

    private static List<String> parseReceiversString(String strRec) {
        return Arrays.stream(strRec.split(SPLITTER)).filter(Regex::validate).toList();
    }
}