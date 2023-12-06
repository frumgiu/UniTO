package com.prog3.project.client.controllers;

import com.example.common.Email;
import com.example.common.Operation;
import com.prog3.project.client.NewEmail;
import com.prog3.project.client.connection.Connection;
import com.prog3.project.client.model.Client;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class ContentController {

    public Label lblSubject;
    public Label lblFrom;
    public Label lblTo;
    public Label lblDate;
    public Button btnClear;
    public Button btnDelete;
    public Button btnReply;
    public Button btnReplyAll;
    public Button btnForward;
    @FXML
    private TextArea emailText;
    @FXML
    private Label emailSubject;
    @FXML
    private Label emailSender;
    @FXML
    private Label emailReceivers;
    @FXML
    private Label emailDate;
    @FXML
    private VBox borderPane;

    private Client model;
    private Connection connection;

    public void init(Client model, Connection connection){

        this.connection = connection;
        this.model = model;
        borderPane.setVisible(false);

        /*
         * Add listener to the property of the model currentEmail.
         * When this property change we will bind all the property of the view's Labels
         * with the property currentEmail of the model.
         */
        this.model.getCurrentEmailProperty().addListener((obs, oldEmail, newEmail) -> {
            if (newEmail != null) {
                emailSubject.textProperty().setValue(newEmail.getSubject());
                emailSender.textProperty().setValue(newEmail.getSender());
                emailReceivers.textProperty().setValue(newEmail.receiversToString());
                emailText.textProperty().setValue(newEmail.getEmailText());
                emailDate.textProperty().setValue(Email.simpleDateFormat.format(newEmail.getDate()));
                borderPane.setVisible(true);
            }
            else {
                borderPane.setVisible(false);
            }
        });
        btnClear.setTooltip(setTooltip("Chiudi l'email"));
        btnDelete.setTooltip(setTooltip("Elimina questo messaggio"));
        btnReply.setTooltip(setTooltip("Rispondi"));
        btnReplyAll.setTooltip(setTooltip("Rispondi a tutti"));
        btnForward.setTooltip(setTooltip("Inoltra"));
    }

    @FXML
    public void onClearButtonClick() {
        model.setCurrentEmail(null);
    }

    @FXML
    public void onDeleteButtonClick() {
        Thread executor = new Thread(() -> {
            if (connection.communicate(Operation.DELETE_EMAIL, model.getCurrentEmailPropertyValue()))
                model.setCurrentEmail(null);
            else {
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
        executor.start();
    }

    @FXML
    public void onReplyButtonClick() {
        NewEmail newEmail = new NewEmail(model, connection, Operation.REPLY_EMAIL,"New Email");
        newEmail.run();
    }

    @FXML
    public void onReplyAllButtonClick() {
        NewEmail newEmail = new NewEmail(model, connection, Operation.REPLY_ALL_EMAIL,"New Email");
        newEmail.run();
    }

    @FXML
    public void onForwardButtonClick() {
        NewEmail newEmail = new NewEmail(model, connection, Operation.FORWARD_MESSAGE,"New Email");
        newEmail.run();
    }

    private static Tooltip setTooltip(String message) {
        Tooltip tooltip = new Tooltip(message);
        tooltip.setStyle("-fx-font-size: 14");
        tooltip.setShowDelay(Duration.millis(500));
        return tooltip;
    }
}