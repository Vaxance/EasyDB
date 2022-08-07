package de2.flp;

import de.flp.easyDB.EasyDB;

/**
 * @author FlorianLetsPlays
 * @version 1.0
 */
public class Main {

    public static EasyDB easyDB;

    public static void main(String[] args) {

        easyDB = new EasyDB("127.0.0.1", "3306", "root", "", "testdb");

        EasyDB.getInstance().getRepositoryManager().addRepository(new Book());

        Book book = (Book) Book.get("expoWabern", Book.class);

        System.out.println(book.name + " hat " + book.seiten + " Seiten!");

        book.seiten = "99";

        book.save("expoWabern");

        while (true) {}

    }




}
