package com.prog3.project.server.model;

import com.example.common.Message;
import com.example.common.Operation;
import java.io.*;
import java.net.*;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Server implements Runnable {

    private final ServerModel model;
    private ServerSocket serverSocket;
    private final ExecutorService pool;
    private boolean isRunning;
    private final int serverPort;
    private final String host;

    public Server(ServerModel model, int port, int poolNum) {
        this.model = model;
        pool = Executors.newFixedThreadPool(poolNum, (Runnable r) -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });
        serverPort = port;
        host = "127.0.0.1";
        isRunning = true;
    }

    public void run() {
        try {
            serverSocket = new ServerSocket(serverPort);
            System.out.println("Server running on port " + this.serverPort + " ...");
            model.addLog(new Message(Operation.SERVER_ACTIVE.getType(), Operation.SERVER_ACTIVE.getMsg()));

            while (isRunning) {
                acceptConnection();
            }
            model.addLog(new Message(Operation.SERVER_STOPPED.getType(), Operation.SERVER_STOPPED.getMsg()));
            System.out.println("Server stopped");
        } catch (IOException e) {
            pool.shutdown();
            e.printStackTrace();
        }
    }

    /* Accept and execute new client connection */
    private void acceptConnection() throws IOException {
        try {
            pool.execute(new Handler(serverSocket.accept()));
        } catch (SocketTimeoutException e) {
            System.out.println("Timeout!");
        } catch (SocketException e) {
            System.out.println("Closing ServerSocket...");
        }
    }

    public void stop() {
        pool.shutdown(); // Disable new tasks from being submitted
        isRunning = false;
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*Stop the executor service*/
        try {
            System.out.println("Closing pool...");
            if (!pool.awaitTermination(30, TimeUnit.SECONDS)) {   // Wait a while for existing tasks to terminate
                pool.shutdownNow();  // Cancel currently executing tasks
                if (!pool.awaitTermination(30, TimeUnit.SECONDS))  // Wait a while for tasks to respond to being cancelled
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    class Handler implements Runnable {
        Socket socket;
        ObjectInputStream inStream = null;
        ObjectOutputStream outStream = null;
        Message messageFromClient;

        public Handler(Socket poolSocket) {
            socket = poolSocket;
        }

        public void run() {
            try {
                openStreams();
                messageFromClient = (Message) inStream.readObject();
                System.out.println("Client " + messageFromClient.getClientName() + " connected!");
                model.addLog(new Message(Operation.SERVER_CONNECTION_WITH_CLIENT.getType(), Operation.SERVER_CONNECTION_WITH_CLIENT.getMsg()
                        + messageFromClient.getClientName()));
                model.addLog(messageFromClient);

                if (messageFromClient.isValid()) {
                    switch (Objects.requireNonNull(Operation.getName(messageFromClient.getType()))) {
                        case GET_INBOX_REQUEST -> outStream.writeObject(model.loadInbox(messageFromClient.getClientName()));
                        case GET_OUTBOX_REQUEST -> outStream.writeObject(model.loadOutbox(messageFromClient.getClientName()));
                        case READ_EMAIL -> outStream.writeObject(model.setReadEmail(messageFromClient.getClientName(), messageFromClient.getEmailsList().get(0)));
                        case DELETE_EMAIL -> outStream.writeObject(model.deleteEmail(messageFromClient.getClientName(), messageFromClient.getEmailsList().get(0)));
                        case SEND_NEW_EMAIL, REPLY_EMAIL, REPLY_ALL_EMAIL, FORWARD_MESSAGE ->
                                outStream.writeObject(model.addEmailToInboxReceivers(messageFromClient.getEmailsList().get(0)));
                        default -> outStream.writeObject(Message.failure(host, messageFromClient.getClientName()));
                    }
                } else {
                    outStream.writeObject(Message.failure(host, messageFromClient.getClientName()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } finally {
                closeStreams();
                System.out.println("End communication");
                model.addLog(new Message(Operation.SERVER_CLOSE_CONNECTION_WITH_CLIENT.getType(), Operation.SERVER_CLOSE_CONNECTION_WITH_CLIENT.getMsg()
                        + messageFromClient.getClientName()));
            }
        }

        private void openStreams() throws IOException {
            inStream = new ObjectInputStream(socket.getInputStream());
            outStream = new ObjectOutputStream(socket.getOutputStream());
        }

        private void closeStreams() {
            try {
                if (inStream != null)
                    inStream.close();
                if (outStream != null)
                    outStream.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}