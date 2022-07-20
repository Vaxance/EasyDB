package de.flp.easyDB.repositories;

import de.flp.easyDB.EasyDB;
import de.flp.easyDB.repositories.anotaions.Field;
import de.flp.easyDB.repositories.anotaions.Fields;
import de.flp.easyDB.repositories.anotaions.RepositoryTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author FlorianLetsPlays
 * @version 1.0
 */
public class RepositoryManager {

    private HashMap<String, Repository> repoMap;
    public RepositoryManager() {
        repoMap = new HashMap<>();
    }

    public void addRepository(Repository repository) {
        createTable(repository);
        repoMap.put(repository.getClass().getDeclaredAnnotation(RepositoryTable.class).tableName(), repository);
    }


    public void createTable(Repository repository) {
        List<String> fields = new ArrayList<>();

        for(Field field : repository.getClass().getDeclaredAnnotation(Fields.class).fields()) {
            fields.add(field.fieldName());
        }

        EasyDB.getInstance().getMySQLConnector().createTable(repository.getClass().getDeclaredAnnotation(RepositoryTable.class).tableName(), fields.toArray(new String[0]));
    }

}
