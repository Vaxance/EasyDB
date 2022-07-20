package de.flp.easyDB.utils;

import de.flp.easyDB.repositories.anotaions.RepositoryTable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author FlorianLetsPlays
 * @version 1.0
 */
public class MySQLConnector {
    private final String host;
    private final String port;
    private final String username;
    private final String password;
    private final String database;
    private static Connection connection;

    public MySQLConnector(String host, String port, String username, String password, String database) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.database = database;
        connect();
    }

    public void connect() {


        if (!isConnected()) {

            try {
                connection = DriverManager.getConnection("jdbc:mysql://37.114.47.122:19137/testdb", username, password);
                System.out.println("MySQL-Verbindung gestartet!");
            } catch (Exception e) {
                System.out.println("MySQL-Verbindung konnte nicht gestartet werden!");
                e.printStackTrace();
            }

        }


    }

    public void close() {

        if (isConnected()) {

            try {
                connection.close();
                System.out.println("MySQL-Verbindung getrennt!");
            } catch (SQLException e) {
                System.out.println("MySQL-Verbindung konnte nicht getrennt werden!");
            }

        }

    }

    public boolean isConnected() {
        return connection != null;
    }

  //todo recode for system

    public void createTable(String tableName, String[] fields) {

        if (!isConnected()) connect();

        if (isConnected()) {

            String querry = "CREATE TABLE IF NOT EXISTS " + tableName + " (";

            boolean first = true;

            for (String field : fields) {
                if (first) {
                    querry = querry + field + " VARCHAR(255)";
                    first = false;
                }
                else querry = querry + ", " + field + "  VARCHAR(255)";
            }

            querry = querry + ")";

            try {
                System.out.println(querry);
                connection.createStatement().executeUpdate(querry);
            } catch (Exception e) {
                System.out.println("Die KnockOut-Tabellen konnten nicht erstellt werden!");
                e.printStackTrace();
            }
        }

    }

    public String getResult(HashMap<String, String> requires, String get) {

        AtomicReference<String> querry = new AtomicReference<>("SELECT * FROM " + getClass().getDeclaredAnnotation(RepositoryTable.class).tableName() + " WHERE ");

        AtomicBoolean first = new AtomicBoolean(true);
        requires.forEach((field, contains) -> {
            if (!first.get())  querry.set(querry + " AND ");
            querry.set(querry  + field + "='" + contains + "'");
            first.set(false);
        });
        final ResultSet rs = getResult(querry.toString());
        try {
            if (rs.next()) {
                final String string = rs.getString(get);
                return string;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        }
        return "";
    }

    public void update(String qry) {

        if (!isConnected()) connect();

        if (isConnected()) {

            try {
                connection.createStatement().executeUpdate(qry);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

    }

    public ResultSet getResult(String qry) {

        if (!isConnected()) connect();

        if (isConnected()) {
            try {
                return connection.createStatement().executeQuery(qry);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

}
