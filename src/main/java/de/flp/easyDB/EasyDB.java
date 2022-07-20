package de.flp.easyDB;

import de.flp.easyDB.repositories.RepositoryManager;
import de.flp.easyDB.utils.MySQLConnector;

/**
 * @author FlorianLetsPlays
 * @version 1.0
 */
public class EasyDB {

    private static EasyDB instance;
    private MySQLConnector mySQLConnector;
    private RepositoryManager repositoryManager;

    public EasyDB(String host, String port, String username, String password, String database) {
        instance = this;
        mySQLConnector = new MySQLConnector(host, port, username, password, database);

        System.out.println("tets");
        repositoryManager = new RepositoryManager();
    }


    //--[getter/setter]
    public MySQLConnector getMySQLConnector() {
        return mySQLConnector;
    }

    public void setMySQLConnector(MySQLConnector mySQLConnector) {
        this.mySQLConnector = mySQLConnector;
    }

    public RepositoryManager getRepositoryManager() {
        return repositoryManager;
    }

    public void setRepositoryManager(RepositoryManager repositoryManager) {
        this.repositoryManager = repositoryManager;
    }

    public static EasyDB getInstance() {
        return instance;
    }
}
