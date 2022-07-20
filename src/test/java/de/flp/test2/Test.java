package de.flp.test2;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author FlorianLetsPlays
 * @version 1.0
 */
public class Test {

    public static void main(String[] args) {
        HashMap<String, String> lol = new HashMap<>();
        lol.put("name", "florian");
        lol.put("age", "16");
        lol(lol);
    }


    public static void lol(HashMap<String, String> requires) {
        AtomicReference<String> querry = new AtomicReference<>("SELECT * FROM " + "players" + " WHERE ");

        AtomicBoolean first = new AtomicBoolean(true);
        requires.forEach((field, contains) -> {
            if (!first.get())  querry.set(querry + " AND ");
            querry.set(querry  + field + "='" + contains + "'");
            first.set(false);
        });

        System.out.println(querry.toString());
    }

}
