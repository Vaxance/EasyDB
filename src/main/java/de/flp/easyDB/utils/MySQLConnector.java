package de.flp.easyDB.utils;

import de.flp.easyDB.repositories.Repository;
import de.flp.easyDB.repositories.anotaions.Field;
import de.flp.easyDB.repositories.anotaions.Fields;
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
                connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "", username, password);
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
                connection.createStatement().executeUpdate(querry);
            } catch (Exception e) {
                System.out.println("Die KnockOut-Tabellen konnten nicht erstellt werden!");
                e.printStackTrace();
            }
        }

    }

    public String getResult(HashMap<String, String> requires, String get, Class<Repository> clazz) {

        AtomicReference<String> querry = new AtomicReference<>("SELECT * FROM " + clazz.getDeclaredAnnotation(RepositoryTable.class).tableName() + " WHERE ");

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

    public void insert(HashMap<String, String> requires, Class<Repository> clazz) {

        AtomicReference<String> querry = new AtomicReference<>("INSERT INTO " + clazz.getDeclaredAnnotation(RepositoryTable.class).tableName() + " (");

        Field[] fields = clazz.getDeclaredAnnotation(Fields.class).fields();
        for (int i = fields.length; i > 0; i--) {
            if(i>1) querry.set(querry + fields[i-1].fieldName() + ", ");
            else querry.set(querry + fields[i-1].fieldName());
        }

        querry.set(querry + ") VALUES (");

        for (int i = fields.length; i > 0; i--) {
            if(i>1) {
                if(requires.containsKey(fields[i-1].fieldName())) querry.set(querry + "'" + requires.get(fields[i-1].fieldName()) + "',");
                else querry.set(querry + "'none',");
            } else {
                if(requires.containsKey(fields[i-1].fieldName())) querry.set(querry + "'" + requires.get(fields[i-1].fieldName()) + "')");
                else querry.set(querry + "'none'");
            }
        }
        use(querry.toString());
    }

    public void delete(HashMap<String, String> requires, Class<Repository> clazz) {
        AtomicReference<String> querry = new AtomicReference<>("DELETE FROM " + clazz.getDeclaredAnnotation(RepositoryTable.class).tableName() + " WHERE ");
        AtomicBoolean first = new AtomicBoolean(true);
        requires.forEach((field, contains) -> {
            if (!first.get())  querry.set(querry + " AND ");
            querry.set(querry  + field + "='" + contains + "'");
            first.set(false);
        });

        use(querry.toString());
    }
    public void update(HashMap<String, String> requires, String setField, String setValue, Class<Repository> clazz) {
        AtomicReference<String> querry = new AtomicReference<>("UPDATE " + clazz.getDeclaredAnnotation(RepositoryTable.class).tableName() + " SET " + setField + "='" + setValue + "' WHERE ");

        AtomicBoolean first = new AtomicBoolean(true);
        requires.forEach((field, contains) -> {
            if (!first.get())  querry.set(querry + " AND ");
            querry.set(querry  + field + "='" + contains + "'");
            first.set(false);
        });

        use(querry.toString());
    }

    private void use(String qry) {

        if (!isConnected()) connect();

        if (isConnected()) {

            try {
                connection.createStatement().executeUpdate(qry);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

    }

    private ResultSet getResult(String qry) {

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
