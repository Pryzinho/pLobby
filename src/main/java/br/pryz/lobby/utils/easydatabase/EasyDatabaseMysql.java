package br.pryz.lobby.utils.easydatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class EasyDatabaseMysql implements EasyDatabase {

    private String host;
    private String username;
    private String password;
    private String database;
    private Connection connection;

    public EasyDatabaseMysql(String host, String username, String password, String database) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.database = database;
    }

    @Override
    public Connection getConnection() throws EasyDatabaseException {
        try {
            if (connection == null || connection.isClosed())
                openConnection();

            return connection;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public void openConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + host + "/" + database + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", username, password);
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed())
                connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
