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
public abstract class Repository {

    private static HashMap<String, String> request;
    public Repository() {
        request = new HashMap<>();
    }

    public void createTable() {
        List<String> fields = new ArrayList<>();

        for(Field field : getClass().getDeclaredAnnotation(Fields.class).fields()) {
            fields.add(field.fieldName());
        }

        EasyDB.getInstance().getMySQLConnector().createTable(getClass().getDeclaredAnnotation(RepositoryTable.class).tableName(), fields.toArray(new String[0]));
    }

    public FieldItem field(String field) {
        return new FieldItem(field, this);
    }

    public static Repository sendToRepo(Repository repository, String field,  String contains) {
        request.put(field, contains);
        return repository;
    }

    public String get(String field) {
        return EasyDB.getInstance().getMySQLConnector().getResult(request, field);
    }
}
