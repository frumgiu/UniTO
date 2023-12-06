package com.prog3.project.client;

import com.prog3.project.client.connection.Connection;
import com.example.common.Operation;
import com.prog3.project.client.controllers.NewEmailController;
import com.prog3.project.client.model.Client;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class NewEmail implements Runnable {

    private final Client model;
    private final Connection connection;
    private final Operation operation;
    private final String stageTitle;

    /**
     * Constructor
     * @param model (client)
     * @param connection (socket connection)
     * @param operation that the user want to do (send,reply,forward)
     * @param stageTitle Title of the stage
     */
    public NewEmail(Client model, Connection connection, Operation operation, String stageTitle) {
        this.model = model;
        this.connection = connection;
        this.operation = operation;
        this.stageTitle = stageTitle;
    }

    @Override
    public void run() {
        try {
            VBox newEmailPane;
            FXMLLoader loader = new FXMLLoader(ClientLauncher.class.getResource("new-email.fxml"));
            newEmailPane = loader.load();
            NewEmailController controller = loader.getController();

            Scene scene = new Scene(newEmailPane);
            Stage stage = new Stage();

            controller.init(model, connection, stage, operation);
            stage.setTitle(stageTitle);
            scene.getStylesheets().add(Objects.requireNonNull(ClientLauncher.class.getResource("stylesheet/new_email_style.css")).toExternalForm());
            stage.setMinHeight(400);
            stage.setMinWidth(500);
            stage.setHeight(500);
            stage.setWidth(800);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}