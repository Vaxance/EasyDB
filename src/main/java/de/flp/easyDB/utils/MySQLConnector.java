package de.flp.easyDB.utils;

import de.flp.easyDB.async.DataResult;
import de.flp.easyDB.repositories.anotaions.RepositoryTable;
import de.flp.easyDB.repositories.objekdBasedRepos.ObjektRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
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

    private static final Map<Class<?>, String> tableNames = new HashMap<>();

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


    //--ObjektBased--//

    public void createTable(String tableName) {

            if (!isConnected()) connect();

            if (isConnected()) {
                try {
                    use("CREATE TABLE IF NOT EXISTS " + tableName + " (input VARCHAR(255), save VARCHAR(255))");
                } catch (Exception e) {
                    System.out.println("Die Tabellen konnten nicht erstellt werden!");
                    e.printStackTrace();
                }
            }

    }

    public String getResults(String key, Class<ObjektRepository> clazz) {
        final ResultSet rs = getResult("SELECT * FROM " + clazz.getDeclaredAnnotation(RepositoryTable.class).tableName() + " WHERE input='" + key + "'");
        try {
            if (rs.next()) {
                final String string = rs.getString("save");
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
        return null;
    }

    public void insert(String key, String save, Class<ObjektRepository> clazz) {
        String insert = "INSERT INTO " + clazz.getDeclaredAnnotation(RepositoryTable.class).tableName() + " (input, save) VALUES (?, ?)";
        try{
            PreparedStatement ps = connection.prepareStatement(insert);
            ps.setString(1, key);
            ps.setString(2, save);

            ps.executeUpdate();
        } catch (Exception e) {}

    }

    public void update(String key, String save, Class<ObjektRepository> clazz) {
        String update = "UPDATE " + clazz.getDeclaredAnnotation(RepositoryTable.class).tableName() + " SET save=? WHERE input=?";
        try{
            System.out.println(update);
            PreparedStatement ps = connection.prepareStatement(update);
            ps.setString(1, save);
            ps.setString(2, key);

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //--Data-based--//
    public void createTable(String tableName, String[] fields) {

        CompletableFuture.runAsync(() -> {
            if (!isConnected()) connect();

            if (isConnected()) {

                String querry = "CREATE TABLE IF NOT EXISTS " + tableName + " (";

                boolean first = true;

                for (String field : fields) {
                    if (first) {
                        querry += field + " VARCHAR(255)";
                        first = false;
                    } else querry += ", " + field + "  VARCHAR(255)";
                }

                querry = querry + ")";

                try {
                    connection.createStatement().executeUpdate(querry);
                } catch (Exception e) {
                    System.out.println("Die Tabellen konnten nicht erstellt werden!");
                    e.printStackTrace();
                }
            }

        });

    }

    public String getResult(HashMap<String, String> requires, String get, Class<?> clazz) {
        mapClass(clazz);
        String query = "SELECT * FROM " + tableNames.get(clazz) + " WHERE ";

        boolean first = true;

        for (String field : requires.keySet()) {
            if (!first) query += " AND ";
            query += field + "='" + requires.get(field) + "'";
            first = false;
        }
        final ResultSet rs = getResult(query);
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

    public void getResultAsync(HashMap<String, String> requires, String get, Class<?> clazz, DataResult dataResult) {
        CompletableFuture.runAsync(() -> {
            mapClass(clazz);
            String query = "SELECT * FROM " + tableNames.get(clazz) + " WHERE ";

            boolean first = true;

            for (String field : requires.keySet()) {
                if (!first) query += " AND ";
                query += field + "='" + requires.get(field) + "'";
                first = false;
            }
            final ResultSet rs = getResult(query);
            try {
                if (rs.next()) {
                    final String string = rs.getString(get);
                    dataResult.handle(string);
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
        });
    }

    public void insert(HashMap<String, String> requires, Class<?> clazz) {
        mapClass(clazz);

        String query = "INSERT INTO " + tableNames.get(clazz) + " (";

        List<String> values = new ArrayList<>();
        for (String field : requires.keySet()) values.add("'" + requires.get(field) + "'");

        query += requires.keySet().toString().replace("[", "").replace("]", "") + ") VALUES (";
        query += values.toString().replace("[", "").replace("]", "") + ")";
        use(query);
    }

    public void delete(HashMap<String, String> requires, Class<?> clazz) {
        mapClass(clazz);
        String query = "DELETE FROM " + tableNames.get(clazz) + " WHERE ";
        boolean first = true;

        for (String field : requires.keySet()) {
            if (!first) query += " AND ";
            query += field + "='" + requires.get(field) + "'";
            first = false;
        }

        use(query);
    }

    public void update(HashMap<String, String> requires, String setField, String setValue, Class<?> clazz) {
        mapClass(clazz);
        AtomicReference<String> querry = new AtomicReference<>("UPDATE " + tableNames.get(clazz) + " SET " + setField + "='" + setValue + "' WHERE ");

        AtomicBoolean first = new AtomicBoolean(true);
        requires.forEach((field, contains) -> {
            if (!first.get()) querry.set(querry + " AND ");
            querry.set(querry + field + "='" + contains + "'");
            first.set(false);
        });

        use(querry.toString());
    }

    public void mapClass(Class<?> clazz) {
        if (!tableNames.containsKey(clazz))
            tableNames.put(clazz, clazz.getDeclaredAnnotation(RepositoryTable.class).tableName());
    }

    //--API--//

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
