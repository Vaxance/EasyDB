package de.flp.easyDB.repositories;

import de.flp.easyDB.EasyDB;
import de.flp.easyDB.repositories.anotaions.Field;
import de.flp.easyDB.repositories.anotaions.Fields;
import de.flp.easyDB.repositories.anotaions.RepositoryTable;
import de.flp.easyDB.repositories.dataBasedRepos.Repository;
import de.flp.easyDB.repositories.objekdBasedRepos.ObjektRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author FlorianLetsPlays
 * @version 1.0
 */
public class RepositoryManager {

    public RepositoryManager() {
    }

    public void addRepository(Repository repository) {
        createTable(repository);
    }

    public void addRepository(ObjektRepository repository) {
        EasyDB.getInstance().getMySQLConnector().createTable(repository.getClass().getDeclaredAnnotation(RepositoryTable.class).tableName());
    }

    public void createTable(Repository repository) {
        List<String> fields = new ArrayList<>();

        for(Field field : repository.getClass().getDeclaredAnnotation(Fields.class).fields()) {
            fields.add(field.fieldName());
        }

        EasyDB.getInstance().getMySQLConnector().createTable(repository.getClass().getDeclaredAnnotation(RepositoryTable.class).tableName(), fields.toArray(new String[0]));
    }

}
