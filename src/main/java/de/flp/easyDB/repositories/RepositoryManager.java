package de.flp.easyDB.repositories;

import java.util.HashMap;

/**
 * @author FlorianLetsPlays
 * @version 1.0
 */
public class RepositoryManager {

    private HashMap<Repository, String> repoMap;
    public RepositoryManager() {
        repoMap = new HashMap<>();
    }

    public void addRepository(Repository repository) {
        repository.createTable();
        repoMap.put(repository, "model");
    }

}
