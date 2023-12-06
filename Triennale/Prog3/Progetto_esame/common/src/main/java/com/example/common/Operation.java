package com.example.common;

public enum Operation {

    //Server management
    SERVER_ACTIVE(0, "Il server e' in ascolto sulla porta 3306"),
    SERVER_STOPPED(1, "Il server e' stato spento"),
    SERVER_CONNECTION_WITH_CLIENT(2, "Un client si e' connesso al server. "),
    SERVER_CLOSE_CONNECTION_WITH_CLIENT(3, "Un client ha chiuso la connessione con il server. "),

    //Server response to client
    SERVER_RESPONSE_SUCCESS(6,"Operazione eseguita con successo"),
    SERVER_RESPONSE_FAILURE(7,"Operazione fallita"),
    SERVER_RESPONSE_INVALID_EMAIL(8, "Operazione fallita: impossibile inviare l'email"),
    GET_INBOX_RESPONSE(10, "Il client ha ricevuto tutte le sue email ricevute"),
    GET_OUTBOX_RESPONSE(11, "Il client ha ricevuto tutte le sue email inviate"),

    //Client action
    GET_INBOX_REQUEST(13, "Il client ha richiesto tutte le sue email ricevute"),
    GET_OUTBOX_REQUEST(14, "Il client ha richiesto tutte le sue email inviate"),
    DELETE_EMAIL(16, "Il client vuole eliminare una email"),
    READ_EMAIL(17, "Il client ha letto una email nuova"),
    REPLY_EMAIL(18, "Il client vuole inviare una email di risposta"),
    REPLY_ALL_EMAIL(19, "Il client vuole inviare una email di risposta a tutti"),
    FORWARD_MESSAGE(20, "Il client vuole inoltrare una email"),
    SEND_NEW_EMAIL(21, "Il client vuole inviare una nuova email");

    private final int type;
    private final String msg;
    private Runnable method;

    Operation(int type, String msg){
        this.type = type;
        this.msg = msg;
        this.method = null;
    }

    public static Operation getName(int type) {
        return switch (type) {
            case 6 -> Operation.SERVER_RESPONSE_SUCCESS;
            case 7 -> Operation.SERVER_RESPONSE_FAILURE;
            case 8 -> Operation.SERVER_RESPONSE_INVALID_EMAIL;
            case 10 -> Operation.GET_INBOX_RESPONSE;
            case 11 -> Operation.GET_OUTBOX_RESPONSE;

            case 13 -> Operation.GET_INBOX_REQUEST;
            case 14 -> Operation.GET_OUTBOX_REQUEST;
            case 16 -> Operation.DELETE_EMAIL;
            case 17 -> Operation.READ_EMAIL;
            case 18 -> Operation.REPLY_EMAIL;
            case 19 -> Operation.REPLY_ALL_EMAIL;
            case 20 -> Operation.FORWARD_MESSAGE;
            case 21 -> Operation.SEND_NEW_EMAIL;

            default -> null;
        };
    }

    public int getType() {
        return type;
    }

    public String getMsg() {
        return msg;
    }

    public void setOperationExecutor(Runnable executor){
        method = executor;
    }

    public void runOperationExecutor(){
        method.run();
    }
}