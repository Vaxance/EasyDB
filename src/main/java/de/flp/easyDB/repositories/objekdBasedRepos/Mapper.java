package de.flp.easyDB.repositories.objekdBasedRepos;

import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author FlorianLetsPlays
 * @version 1.0
 */
public class Mapper {

    @SneakyThrows
    public static JSONObject map(Object object) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("_clazz", object.getClass().getName());
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (isMappable(field.getType())) {
                jsonObject.put(field.getName(), field.get(object));
            } else {
                jsonObject.put(field.getName(), map(field.get(object)));
            }
        }
        return jsonObject;
    }

    @SneakyThrows
    public static Object unmap(String s) {
        JSONObject jsonObject = new JSONObject(s);
        Class<?> clazz = Class.forName(jsonObject.get("_clazz").toString());
        Object object = clazz.newInstance();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (isMappable(field.getType())) {
                if (jsonObject.get(field.getName()) instanceof JSONArray) {
                    List<Object> list = new ArrayList<>();
                    for (Object obj : (JSONArray) jsonObject.get(field.getName())) {
                        list.add(obj);
                    }
                    field.set(object, list);
                } else {
                    field.set(object, jsonObject.get(field.getName()));
                }
            } else {
                field.set(object, unmap(jsonObject.get(field.getName()).toString()));
            }
        }
        return object;
    }

    private static boolean isMappable(Class<?> clazz) {
        return clazz.equals(String.class) ||
                clazz.equals(Integer.class) ||
                clazz.equals(Long.class) ||
                clazz.equals(Double.class) ||
                clazz.equals(Float.class) ||
                clazz.isPrimitive() ||
                clazz.equals(List.class);
    }

}

