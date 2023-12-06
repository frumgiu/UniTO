package com.prog3.project.client.controllers;

import com.example.common.Email;
import com.prog3.project.client.ClientLauncher;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import java.io.IOException;

public class EmailListViewCell extends ListCell<Email> {

    @FXML
    private Label cardEmailSender;
    @FXML
    private Label cardEmailSubject;
    @FXML
    private Label cardEmailContent;
    @FXML
    private Circle cardRead;
    @FXML
    private GridPane cardPanel;
    @FXML
    private FXMLLoader mLLoader;

    {
        setStyle("-fx-padding: 10px 0px 10px 0px");
    }

    @Override
    protected void updateItem(Email email, boolean empty) {
        super.updateItem(email, empty);
        if (empty || email == null) {
            setText(null);
            setGraphic(null);
        }
        else {
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(ClientLauncher.class.getResource("card-email.fxml"));
                mLLoader.setController(this);
                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            cardEmailSender.setText(email.getSender());
            cardEmailSubject.setText(email.getSubject());
            cardEmailContent.setText(Email.simpleDateFormat.format(email.getDate()));
            cardRead.visibleProperty().setValue(email.isNew());
            setText(null);
            setGraphic(cardPanel);
        }
    }
}