package com.prog3.project.client;

import com.prog3.project.client.connection.Connection;
import com.prog3.project.client.controllers.RootController;
import com.prog3.project.client.model.Client;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class ClientLauncher extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        /* Get the args from main method */
        Parameters parameters = getParameters();
        String clientEmailAddress = parameters.getRaw().get(0);

        Client model = new Client(clientEmailAddress);
        Connection connection = new Connection(model);

        FXMLLoader rootLoader = new FXMLLoader(ClientLauncher.class.getResource("root-view.fxml"));
        rootLoader.setController(new RootController(model, connection));

        Scene scene = new Scene(rootLoader.load());
        scene.getStylesheets().add(Objects.requireNonNull(ClientLauncher.class.getResource("stylesheet/style.css")).toExternalForm());

        stage.setMinHeight(540);
        stage.setMinWidth(1000);
        stage.setHeight(600);
        stage.setWidth(1050);

        stage.setScene(scene);
        stage.setTitle(clientEmailAddress + "'s client mail");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}