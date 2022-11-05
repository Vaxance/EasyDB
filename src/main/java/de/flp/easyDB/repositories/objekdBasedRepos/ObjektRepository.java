package de.flp.easyDB.repositories.objekdBasedRepos;

import com.google.gson.Gson;
import de.flp.easyDB.EasyDB;

import java.util.concurrent.CompletableFuture;

/**
 * @author FlorianLetsPlays
 * @version 1.0
 */
public abstract class ObjektRepository {

    private static Gson gson = new Gson();
    public static Object get(String key, Class clazz) {
        return gson.fromJson(EasyDB.getInstance().getMySQLConnector().getResults(key, clazz), clazz);
        //return Mapper.unmap(EasyDB.getInstance().getMySQLConnector().getResults(key, clazz));
    }

    public void save(String key) {
        CompletableFuture.runAsync(() -> {
            if (EasyDB.getInstance().getMySQLConnector().getResults(key, (Class<ObjektRepository>) getClass()) == null) {
                EasyDB.getInstance().getMySQLConnector().insert(key, gson.toJson(this), (Class<ObjektRepository>) getClass());
            } else {
                EasyDB.getInstance().getMySQLConnector().update(key, gson.toJson(this), (Class<ObjektRepository>) getClass());
            }
        });
    }
}
