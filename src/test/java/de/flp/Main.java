package de.flp;

import de.flp.easyDB.EasyDB;
import de.flp.repo.PlayerRepo;

/**
 * @author FlorianLetsPlays
 * @version 1.0
 */
public class Main {

    private static EasyDB easyDB;

    public static void main(String[] args) {
        easyDB = new EasyDB("127.0.0.1", "3306", "root", "test", "testdb");


        easyDB.getRepositoryManager().addRepository(new PlayerRepo());

        String age = new PlayerRepo().field("name").cointains("dsa").get("age");

    }

    //


}
