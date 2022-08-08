package de.flp.easyDB.utils;

import de.flp.easyDB.async.DataResult;
import de.flp.easyDB.repositories.anotaions.RepositoryTable;
import de.flp.easyDB.repositories.objekdBasedRepos.ObjektRepository;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author FlorianLetsPlays
 * @version 1.0
 */
public class MySQLConnector {
    private static final Map<Class<?>, String> tableNames = new HashMap<>();
    private static Connection connection;
    private final String host;
    private final String port;
    private final String username;
    private final String password;
    private final String database;

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
                PreparedStatement ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + tableName + " (input VARCHAR(255), save VARCHAR(255))");
                ps.executeUpdate();
            } catch (Exception e) {
                System.out.println("Die Tabellen konnten nicht erstellt werden!");
                e.printStackTrace();
            }
        }

    }

    public String getResults(String key, Class<ObjektRepository> clazz) {
        try {
            String querry = "SELECT * FROM " + clazz.getDeclaredAnnotation(RepositoryTable.class).tableName() + " WHERE input=?";
            PreparedStatement ps = connection.prepareStatement(querry);
            ps.setString(1, key);
            ResultSet resultSet = ps.executeQuery();
            try {
                if (resultSet.next()) {
                    final String string = resultSet.getString("save");
                    return string;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    resultSet.close();
                } catch (SQLException e2) {
                    e2.printStackTrace();
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

    public void insert(String key, String save, Class<ObjektRepository> clazz) {
        String insert = "INSERT INTO " + clazz.getDeclaredAnnotation(RepositoryTable.class).tableName() + " (input, save) VALUES (?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(insert);
            ps.setString(1, key);
            ps.setString(2, save);

            ps.executeUpdate();
        } catch (Exception e) {
        }

    }

    public void update(String key, String save, Class<ObjektRepository> clazz) {
        String update = "UPDATE " + clazz.getDeclaredAnnotation(RepositoryTable.class).tableName() + " SET save=? WHERE input=?";
        try {
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

    public Boolean exists(HashMap<String, String> requires, Class<?> clazz) {
        return true;
    }

    public String getResult(HashMap<String, String> requires, String get, Class<?> clazz) {
        try {
            mapClass(clazz);
            String query = "SELECT * FROM " + tableNames.get(clazz) + " WHERE ";

            boolean first = true;

            for (String field : requires.keySet()) {
                if (!first) query += " AND ";
                query += field + "=?";
                first = false;
            }

            PreparedStatement ps = connection.prepareStatement(query);
            int tmp = 1;
            for (String field : requires.keySet()) {
                ps.setString(tmp, requires.get(field));
                tmp++;
            }

            final ResultSet rs = ps.executeQuery();
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
        } catch (Exception e) {
        }
        return "";
    }

    public void getResultAsync(HashMap<String, String> requires, String get, Class<?> clazz, DataResult dataResult) {
        CompletableFuture.runAsync(() -> {
            try {
                mapClass(clazz);
                String query = "SELECT * FROM " + tableNames.get(clazz) + " WHERE ";

                boolean first = true;

                for (String field : requires.keySet()) {
                    if (!first) query += " AND ";
                    query += field + "=?";
                    first = false;
                }

                PreparedStatement ps = connection.prepareStatement(query);
                int tmp = 1;
                for (String field : requires.keySet()) {
                    ps.setString(tmp, requires.get(field));
                    tmp++;
                }

                final ResultSet rs = ps.executeQuery();
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
            } catch (Exception e) {
            }
        });
    }

    public void insert(HashMap<String, String> requires, Class<?> clazz) {
        try {

            mapClass(clazz);

            String query = "INSERT INTO " + tableNames.get(clazz) + " (";


            query += requires.keySet().toString().replace("[", "").replace("]", "") + ") VALUES (";

            for (String field : requires.keySet()) {
                query += "?,";
            }

            query = query.substring(0, query.length() - 1);

            query += ")";

            PreparedStatement ps = connection.prepareStatement(query);

            int tmp = 1;

            for (String field : requires.values()) {
                ps.setString(tmp, field);
                tmp++;
            }

            ps.executeUpdate();

        } catch (Exception e) {
        }
    }

    public void delete(HashMap<String, String> requires, Class<?> clazz) {
        try {
            mapClass(clazz);
            String query = "DELETE FROM " + tableNames.get(clazz) + " WHERE ";

            boolean first = true;

            for (String field : requires.keySet()) {
                if (!first) query += " AND ";
                query += field + "=?";
                first = false;
            }

            PreparedStatement ps = connection.prepareStatement(query);
            int tmp = 1;
            for (String field : requires.keySet()) {
                ps.setString(tmp, requires.get(field));
                tmp++;
            }

            ps.executeUpdate();
        } catch (Exception e) {
        }
    }

    public void update(HashMap<String, String> requires, String setField, String setValue, Class<?> clazz) {
        try {
            mapClass(clazz);
            String query = "UPDATE " + tableNames.get(clazz) + " SET " + setField + "='?' WHERE ";


            boolean first = true;

            for (String field : requires.keySet()) {
                if (!first) query += " AND ";
                query += field + "=?";
                first = false;
            }

            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, setValue);
            int tmp = 2;
            for (String field : requires.keySet()) {
                ps.setString(tmp, requires.get(field));
                tmp++;
            }
        } catch (Exception e) {
        }
    }

    //--API--//
    public void mapClass(Class<?> clazz) {
        if (!tableNames.containsKey(clazz))
            tableNames.put(clazz, clazz.getDeclaredAnnotation(RepositoryTable.class).tableName());
    }


}
