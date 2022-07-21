package de.flp.repo;

import de.flp.easyDB.repositories.dataBasedRepos.Repository;
import de.flp.easyDB.repositories.anotaions.Field;
import de.flp.easyDB.repositories.anotaions.Fields;
import de.flp.easyDB.repositories.anotaions.RepositoryTable;

/**
 * @author FlorianLetsPlays
 * @version 1.0
 */
@RepositoryTable(tableName = "books")
@Fields(fields = {
        @Field(fieldName = "name"),
        @Field(fieldName = "ISBN"),
        @Field(fieldName = "Sides")
})
public class BookSave extends Repository {

    public BookSave() {
    }
}
