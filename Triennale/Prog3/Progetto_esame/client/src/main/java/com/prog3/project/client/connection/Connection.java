package com.prog3.project.client.connection;

import com.example.common.Email;
import com.example.common.Message;
import com.example.common.Operation;
import com.prog3.project.client.model.Client;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.commons.lang3.RandomStringUtils;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;

public class Connection {

    private final Object lock = new Object();
    private static final String host = "127.0.0.1";
    private static final int port = 3306;
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private final Client model;
    private final StringProperty serverConnectionError;
    private final StringProperty serverInvalidUserError;
    private String connection_id;

    /**
     * Constructor
     * @param model (client)
     */
    public Connection(Client model) {
        this.model = model;
        serverConnectionError = new SimpleStringProperty();
        serverInvalidUserError = new SimpleStringProperty("");
        // Setting the setOperationExecutor of the Email
        Operation.DELETE_EMAIL.setOperationExecutor(this::reload);
        Operation.SEND_NEW_EMAIL.setOperationExecutor(this::reload);
        Operation.REPLY_EMAIL.setOperationExecutor(this::reload);
        Operation.REPLY_ALL_EMAIL.setOperationExecutor(this::reload);
        Operation.FORWARD_MESSAGE.setOperationExecutor(this::reload);
        Operation.READ_EMAIL.setOperationExecutor(this::reload);
    }

    public StringProperty serverConnectionErrorProperty() {
        return serverConnectionError;
    }

    public StringProperty ServerInvalidUserErrorProperty() {
        return serverInvalidUserError;
    }

    /**
     * @param type - Operation to execute
     * @param elem - Element passed as parameter
     */
    public boolean communicate(Operation type, Email elem){
        synchronized (lock) {
            connection_id = RandomStringUtils.randomAlphabetic(6);
            serverInvalidUserError.setValue("");
            if (!type.equals(Operation.GET_INBOX_REQUEST) && !type.equals(Operation.GET_OUTBOX_REQUEST))
                serverConnectionError.setValue("search connection");
            int attempts = 0;
            boolean success = false;

            while (attempts < 5 && !success) {
                attempts += 1;
                System.out.println("[ID connection: " + this.connection_id + ", ID client: " + model.getClientEmailPropertyValue() + " ] Tentative nr. " + attempts + "--" +
                        Thread.currentThread().getName());
                success = tryCommunication(type, elem);
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (!success) {
                    serverConnectionErrorProperty().setValue("errore connessione");
            } else
                serverConnectionErrorProperty().setValue("");
            return success;
        }
    }

    private boolean tryCommunication(Operation type, Email elem) {
        try {
            connectToServer(type, elem);
            return true;
        } catch (ConnectException ce) {
            return false;
        } catch (IOException | ClassNotFoundException se) {
            se.printStackTrace();
            return false;
        } finally {
            closeConnections();
        }
    }

    private void connectToServer(Operation op, Email email) throws IOException, ClassNotFoundException {
        socket = new Socket(host, port);
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());
        outputStream.flush();
        Message message;
        if (email != null) {
            ArrayList<Email> emails = new ArrayList<>();
            emails.add(email);
            message = new Message(op.getType(), op.getMsg(), model.getClientEmailPropertyValue(), host, emails);
        } else {
            message = new Message(op.getType(), op.getMsg(), model.getClientEmailPropertyValue());
        }
        outputStream.writeObject(message); //Scrive il messaggio al server
        outputStream.flush();
        message = (Message) inputStream.readObject();  //Riceve la risposta

        Operation receivedOperation = Operation.getName(message.getType());
        switch (receivedOperation) {
            case GET_INBOX_RESPONSE, GET_OUTBOX_RESPONSE -> {
                ArrayList<Email> emailsList = message.getEmailsList();
                if (emailsList != null && emailsList.size() > 0) {
                    String nameList = receivedOperation == Operation.GET_OUTBOX_RESPONSE ? "OUTBOX" : "INBOX";
                    Platform.runLater(() -> model.loadEmails(emailsList, nameList));
                }
            }
            case SERVER_RESPONSE_SUCCESS -> Platform.runLater(op::runOperationExecutor);
            case SERVER_RESPONSE_FAILURE -> System.out.println("SERVER FAILURE");
            case SERVER_RESPONSE_INVALID_EMAIL -> this.serverInvalidUserError.setValue("invalid user");
        }
    }

    private void closeConnections() {
        if (socket != null) {
            try {
                inputStream.close();
                outputStream.close();
                socket.close();
                socket = null;
                System.out.println("[ID : " + this.connection_id + ", ID client: " + model.getClientEmailPropertyValue() +" ] Connection closed -- " + Thread.currentThread().getName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void reload() {
        Thread executor = new Thread(() -> {
            communicate(Operation.GET_INBOX_REQUEST, null);
            communicate(Operation.GET_OUTBOX_REQUEST, null);
        });
        executor.start();
    }
}