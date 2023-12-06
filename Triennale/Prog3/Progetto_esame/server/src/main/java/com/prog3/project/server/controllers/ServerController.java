package com.prog3.project.server.controllers;

import com.example.common.Message;
import com.prog3.project.server.model.Server;
import com.prog3.project.server.model.ServerModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ServerController {

    private Stage mainStage;
    private ServerModel model;
    private Server server;

    @FXML
    public Label serverLbl;
    @FXML
    private Button startStopBtn;
    @FXML
    private ListView<Message> logList;

    public void init(Stage stage){
        model = new ServerModel();
        mainStage = stage;

        logList.itemsProperty().bind(model.messageListPropProperty());
        logList.setCellFactory(logListView -> new MessageListViewCell());
    }

    @FXML
    protected void startButtonClick() {
        if(startStopBtn.getStyleClass().contains("btn-start")){
            if(startServer()) {
                serverLbl.setText("Spegni server");
                startStopBtn.getStyleClass().remove("btn-start");
                startStopBtn.getStyleClass().add("btn-stop");
            }
        }
        else {
            stopServer();
            mainStage.setTitle("Server");
            serverLbl.setText("Avvia server");
            startStopBtn.getStyleClass().remove("btn-stop");
            startStopBtn.getStyleClass().add("btn-start");
        }
    }

    private boolean startServer() {
        Thread thread;
        int port, nPool;
        port = 3306;
        nPool = 10;
        mainStage.setTitle("Server");
        server = new Server(model, port, nPool);
        thread = new Thread(server);
        thread.setDaemon(true);
        thread.start();
        return true;
    }

    private void stopServer() {
        server.stop();
        System.out.println("Shutdown server!");
    }
}