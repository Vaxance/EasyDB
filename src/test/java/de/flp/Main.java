package de.flp;

import de.flp.easyDB.EasyDB;
import de.flp.repo.BookSave;

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

        String s = "";
        start = System.currentTimeMillis();
        s = new BookSave().field("ISBN").cointains("5456465").get("name");

        long outcome = System.currentTimeMillis() - start;
        System.out.println(s + " - " + outcome + "ms");


    }

    //new PlayerRepo().field("name").contains("florian").get().value("");


}
