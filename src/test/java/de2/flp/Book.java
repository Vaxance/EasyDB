package de2.flp;

import de.flp.easyDB.repositories.anotaions.RepositoryTable;
import de.flp.easyDB.repositories.objekdBasedRepos.ObjektRepository;

/**
 * @author FlorianLetsPlays
 * @version 1.0
 */
@RepositoryTable(tableName = "bookssave")
public class Book extends ObjektRepository {

    String name;
    String ISBN;
    String seiten;
    String type;

    public Book(String name, String ISBN, String seiten, String type) {
        this.name = name;
        this.ISBN = ISBN;
        this.seiten = seiten;
        this.type = type;
    }

    public Book() {
    }




}
