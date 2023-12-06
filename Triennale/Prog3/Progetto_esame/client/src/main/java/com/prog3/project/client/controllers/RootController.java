package com.prog3.project.client.controllers;

import com.example.common.Operation;
import com.prog3.project.client.ClientLauncher;
import com.prog3.project.client.NewEmail;
import com.prog3.project.client.connection.Connection;
import com.prog3.project.client.model.Client;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RootController implements Initializable {
    @FXML
    public BorderPane rootPn;
    @FXML
    public HBox buttonPn;
    @FXML
    public Button btnReload;
    @FXML
    public Button btnNewMessage;
    @FXML
    public HBox contentPn;

    private Node listNode;
    private Node contentNode;
    private final Client model;
    private final Connection connection;

    public RootController(Client model, Connection connection) {
        this.model = model;
        this.connection = connection;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FXMLLoader menuLoader = new FXMLLoader(ClientLauncher.class.getResource("menu-view.fxml"));
        FXMLLoader listLoader = new FXMLLoader(ClientLauncher.class.getResource("list-view.fxml"));
        FXMLLoader contentLoader = new FXMLLoader(ClientLauncher.class.getResource("content-view.fxml"));

        try {
            rootPn.setLeft(menuLoader.load());
            listNode = listLoader.load();
            contentNode = contentLoader.load();
            contentPn.getChildren().add(listNode);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        model.getCurrentEmailProperty().addListener((obs, oldEmail, newEmail) -> {
            contentPn.getChildren().clear();
            if (newEmail == null) {
                contentPn.getChildren().add(listNode);
            } else {
                contentPn.getChildren().add(contentNode);
            }
        });

        MenuController menuController = menuLoader.getController();
        ListController listController = listLoader.getController();
        ContentController contentController = contentLoader.getController();
        menuController.init(model, connection);
        listController.init(model, connection);
        contentController.init(model, connection);

        btnReload.setTooltip(setTooltip("Ricarica da server"));
        btnNewMessage.setTooltip(setTooltip("Nuova email"));

        this.connection.serverConnectionErrorProperty().addListener((observableValue, oldError, newError) -> {
            if (!newError.equals("")) {
                Platform.runLater(() -> btnNewMessage.disableProperty().setValue(true));
            } else {
                Platform.runLater(() ->  btnNewMessage.disableProperty().setValue(false));
            }
        });
    }

    @FXML
    public void onClickNewMessage() {
        NewEmail newEmail = new NewEmail(model, connection, Operation.SEND_NEW_EMAIL,"New Email");
        newEmail.run();
    }

    @FXML
    public void onClickReload() {
        Thread executor = new Thread(() -> {
            connection.communicate(Operation.GET_INBOX_REQUEST, null);
            connection.communicate(Operation.GET_OUTBOX_REQUEST, null);
        });
        executor.start();
    }

    private static Tooltip setTooltip(String message) {
        Tooltip tooltip = new Tooltip(message);
        tooltip.setShowDelay(Duration.millis(500));
        return tooltip;
    }
}