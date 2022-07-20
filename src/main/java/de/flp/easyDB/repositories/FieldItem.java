package de.flp.easyDB.repositories;

/**
 * @author FlorianLetsPlays
 * @version 1.0
 */
public class FieldItem {

    private Repository repository;

    private String field;
    public FieldItem(String field, Repository repository) {
        this.field = field;
        this.repository = repository;
    }

    public Repository cointains(String objekt) {
        return Repository.sendToRepo(repository, field, objekt);
    }
}
