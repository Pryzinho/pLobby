package br.pryz.lobby.utils.easydatabase;

public class EasyDatabaseException extends Exception {

    public EasyDatabaseException(Exception ex) {
        super(ex);
    }

    public EasyDatabaseException(String message) {
        super(message);
    }
}
