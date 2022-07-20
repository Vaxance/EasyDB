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
        easyDB = new EasyDB("127.0.0.1", "3306", "root", "", "testdb");


        easyDB.getRepositoryManager().addRepository(new PlayerRepo());

        new PlayerRepo().field("name").cointains("florian").update("age", "16");
        new PlayerRepo().field("name").cointains("jonas").insert();
        new PlayerRepo().field("name").cointains("admin").get("age");
        new PlayerRepo().field("name").cointains("lolGamer").remove();

    }

    //new PlayerRepo().field("name").contains("florian").get().value("");


}
