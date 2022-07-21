package de2.flp;

import de.flp.easyDB.EasyDB;
import de.flp.easyDB.repositories.objekdBasedRepos.Mapper;

/**
 * @author FlorianLetsPlays
 * @version 1.0
 */
public class Main {

    public static EasyDB easyDB;

    public static void main(String[] args) {

        easyDB = new EasyDB("127.0.0.1", "3306", "root", "", "testdb");

        EasyDB.getInstance().getRepositoryManager().addRepository(new Book());


        long start = System.currentTimeMillis();
        Book book = (Book) Book.get("lol3", Book.class);
        long outtime = System.currentTimeMillis() -start;
        System.out.println(outtime);

        while (true) {}

    }




}
