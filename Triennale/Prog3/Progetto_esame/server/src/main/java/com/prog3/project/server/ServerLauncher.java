package com.prog3.project.server;

import com.prog3.project.server.controllers.ServerController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class ServerLauncher extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(ServerLauncher.class.getResource("server-view-test.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        stage.setMinHeight(800);
        stage.setMinWidth(500);

        ServerController serverController = fxmlLoader.getController();

        serverController.init(stage);
        scene.getStylesheets().add(Objects.requireNonNull(ServerLauncher.class.getResource("css/style.css")).toExternalForm());
        stage.setTitle("Server");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}