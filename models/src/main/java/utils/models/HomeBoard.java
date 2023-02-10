package utils.models;

import java.util.List;

public class HomeBoard {

    private List<Content> homeContents;

    // Pattern Singleton --> esiste una sola istanza di HomeBoard
    private static final HomeBoard homeBoard = new HomeBoard();

    /* TODO: aggiungere lista di telegram */
    /* TODO: in content-microservice andrà creato il controller della HomeBoard, fa parte di quel servizio */
}