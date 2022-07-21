package de.flp.easyDB.repositories.dataBasedRepos;

import de.flp.easyDB.EasyDB;
import de.flp.easyDB.async.DataResult;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

/**
 * @author FlorianLetsPlays
 * @version 1.0
 */
public abstract class Repository {

    private static HashMap<String, String> request;
    public Class<Repository> clazz;
    public String outPut = "";

    public Repository() {
        request = new HashMap<>();
    }

    public FieldItem field(String field) {
        return new FieldItem(field, this);
    }

    public static Repository sendToRepo(Repository repository, String field, String contains) {
        request.put(field, contains);
        return repository;
    }

    public void insertAsync() {
        CompletableFuture.runAsync(() -> {
            EasyDB.getInstance().getMySQLConnector().insert(request, getClass());
        });
    }

    public String get(String field) {
        return EasyDB.getInstance().getMySQLConnector().getResult(request, field, getClass());
    }

    public void getAsync(String field, DataResult dataResult) {
        EasyDB.getInstance().getMySQLConnector().getResultAsync(request, field, getClass(), dataResult);
    }

    public void updateAsync(String field, String value) {
        CompletableFuture.runAsync(() -> {
            EasyDB.getInstance().getMySQLConnector().update(request, field, value, getClass());
        });
    }

    public void removeAsync() {
        CompletableFuture.runAsync(() -> {
            EasyDB.getInstance().getMySQLConnector().delete(request, getClass());
        });
    }


}
