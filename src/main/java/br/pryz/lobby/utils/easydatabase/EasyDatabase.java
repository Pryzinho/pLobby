package br.pryz.lobby.utils.easydatabase;

import java.sql.Connection;

public interface EasyDatabase {

    Connection getConnection() throws EasyDatabaseException;

    void openConnection();

    void closeConnection();
}
