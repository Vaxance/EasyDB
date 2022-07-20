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
    public Class<Repository> clazz;
    public Repository() {
        request = new HashMap<>();
    }

    public FieldItem field(String field) {
        return new FieldItem(field, this);
    }

    public static Repository sendToRepo(Repository repository, String field,  String contains) {
        request.put(field, contains);
        return repository;
    }

    public void insert() {
        EasyDB.getInstance().getMySQLConnector().insert(request, (Class<Repository>) getClass());
    }

    public String get(String field) {
        return EasyDB.getInstance().getMySQLConnector().getResult(request, field, (Class<Repository>) getClass());
    }

    public void update(String field, String value) {
        EasyDB.getInstance().getMySQLConnector().update(request, field, value, (Class<Repository>) getClass());
    }

    public void remove() {
        EasyDB.getInstance().getMySQLConnector().delete(request, (Class<Repository>) getClass());
    }


}
