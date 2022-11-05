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

        Book book = new Book("", "", "", "");

        book.save("cleanBook");

        Book book1 = (Book) Book.get("cleanBook", Book.class);

        System.out.println(book.name);

        while (true) {}

    }




}
