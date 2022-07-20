package de.flp.repo;

import de.flp.easyDB.repositories.Repository;
import de.flp.easyDB.repositories.anotaions.Field;
import de.flp.easyDB.repositories.anotaions.Fields;
import de.flp.easyDB.repositories.anotaions.RepositoryTable;

/**
 * @author FlorianLetsPlays
 * @version 1.0
 */
@RepositoryTable(tableName = "players")
@Fields(fields = {
        @Field(fieldName = "name"),
        @Field(fieldName = "age"),
        @Field(fieldName = "UUID")
})
public class PlayerRepo extends Repository {

}
