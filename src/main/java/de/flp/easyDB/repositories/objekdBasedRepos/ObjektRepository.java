package de.flp.easyDB.repositories.objekdBasedRepos;

import de.flp.easyDB.EasyDB;

import java.util.concurrent.CompletableFuture;

/**
 * @author FlorianLetsPlays
 * @version 1.0
 */
public abstract class ObjektRepository {

    public static Object get(String key, Class clazz) {
        return Mapper.unmap(EasyDB.getInstance().getMySQLConnector().getResults(key, clazz));
    }

    public void save(String key) {
        CompletableFuture.runAsync(() -> {
            if (EasyDB.getInstance().getMySQLConnector().getResults(key, (Class<ObjektRepository>) getClass()) == null) {
                EasyDB.getInstance().getMySQLConnector().insert(key, Mapper.map(this).toString(), (Class<ObjektRepository>) getClass());
            } else {
                EasyDB.getInstance().getMySQLConnector().update(key, Mapper.map(this).toString(), (Class<ObjektRepository>) getClass());
            }
        });
    }
}
