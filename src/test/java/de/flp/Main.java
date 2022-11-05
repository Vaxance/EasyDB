package de.flp;

import de.flp.easyDB.EasyDB;
import de.flp.repo.BookSave;
import de2.flp.Book;

/**
 * @author FlorianLetsPlays
 * @version 1.0
 */
public class Main {

    private static EasyDB easyDB;

    private static long start;
    public static void main(String[] args) {
        easyDB = new EasyDB("127.0.0.1", "3306", "root", "", "testdb");

        easyDB.getRepositoryManager().addRepository(new BookSave());

        new BookSave().field("name").cointains("BTD5").updateAsync("Siedes", "30");


    }

    //new PlayerRepo().field("name").contains("florian").get().value("");


}
