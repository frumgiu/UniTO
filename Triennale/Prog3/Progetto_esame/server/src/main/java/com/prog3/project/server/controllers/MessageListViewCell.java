package com.prog3.project.server.controllers;

import com.example.common.Message;
import com.prog3.project.server.ServerLauncher;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageListViewCell extends ListCell<Message> {

    SimpleDateFormat simpleDateFormat;
    @FXML
    private VBox logPanel;
    @FXML
    private Label lblDescription;
    @FXML
    private Label lblSender;
    @FXML
    private Label lblReceiver;
    @FXML
    private Label lblTime;
    @FXML
    private Label infoImg;
    @FXML
    private Label infoLogLbl;
    @FXML
    private FXMLLoader mLLoader;

    @Override
    protected void updateItem(Message message, boolean empty) {
        super.updateItem(message, empty);
        if (empty || message == null) {
            setText(null);
            setGraphic(null);
        }
        else {
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(ServerLauncher.class.getResource("log-cell.fxml"));
                mLLoader.setController(this);
                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Platform.runLater(() -> {
                setLogCellView(message);
                setText(null);
                setGraphic(logPanel);
            });
        }
    }

    private void setLogCellView(Message message) {
        lblDescription.setText(message.getDescription());
        lblTime.setText(Message.simpleDateFormat.format(new Date()));
        switch (message.getType()) {
            case 0, 1, 2, 3 -> {
                setLogLabel(" ", " ", "info stato del server");
                setLogImage("info-system");
            }
            case 6, 9, 10, 11, 12 -> {
                setLogLabel(message.getReceiver(), message.getClientName(), "info operazione");
                setLogImage("info-positive");
            }
            case 7, 8 -> {
                setLogLabel(message.getReceiver(), message.getClientName(), "info operazione");
                setLogImage("info-negative");
            }
            case 13, 14, 15, 16, 17, 18, 19, 20, 21 -> {
                setLogLabel(message.getClientName(), message.getReceiver(), "info richiesta da client");
                setLogImage("info-request-client");
            }
        }
    }

    private void setLogImage(String e) {
        infoImg.getStyleClass().clear();
        infoImg.getStyleClass().add(e);
    }

    private void setLogLabel(String sender, String receiver, String info) {
        lblSender.setText(sender);
        lblReceiver.setText(receiver);
        infoLogLbl.setText(info);
    }
}