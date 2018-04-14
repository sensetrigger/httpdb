package com.sensetrigger.jsonparser;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

import static com.sensetrigger.jsonparser.JSONFormatter.tabs;

public class Parser
{
    Map<Class, JSONFormatter> types = new HashMap<>();

    public static final String BEGIN = "{\n";
    public static final String END = "%s}\n";
    public static final String FIELD = "%s\"%s\": %s";
    public static final String COMMA = ",\n";

    {
        types.put(String.class, new JSONString());
        types.put(Date.class, new JSONDate());
        types.put(Calendar.class, new JSONCalendar());
        types.put(GregorianCalendar.class, new JSONCalendar());
        types.put(Array.class, new JSONArray());
        types.put(HashSet.class, new JSONSet());
        types.put(HashMap.class, new JSONMap());
        types.put(ArrayList.class, new JSONList());
    }

    public String parser(Object obj) {
        StringBuilder result = new StringBuilder();
        Class clazz = obj.getClass();

        if (clazz.isArray())
            return types.get(Array.class).convert(obj.getClass().getSimpleName(), obj, 0);
        else if (isPrimitiveOrWrapper(clazz))
            result.append(String.format(FIELD, "", obj.getClass().getSimpleName(), obj));
        else if (types.get(clazz) != null)
            return types.get(clazz).convert(obj.getClass().getSimpleName(), obj, 0);
        else
            result = getFields(obj, clazz,0);

        return result.toString();
    }

    public StringBuilder getFields(Object obj, Class clazz, int tabs) {
        StringBuilder result = new StringBuilder();
        result.append(String.format(BEGIN, tabs(tabs)));
        tabs++;
        try {
            Field[] fields = clazz.getDeclaredFields();

            for (int i = 0; i < fields.length; i++) {
                if (isPrimitiveOrWrapper(fields[i].get(obj).getClass()))
                    result.append(String.format(FIELD, tabs(tabs), fields[i].getName(), fields[i].get(obj)));
                else if (fields[i].get(obj).getClass().isArray()) {
                    result.append(types.get(Array.class).convert(fields[i].getName(), fields[i].get(obj), tabs));
                }
                else
                    result.append(types.get(fields[i].get(obj).getClass()).convert(fields[i].getName(), fields[i].get(obj), tabs));

                result.append(((i < fields.length - 1) || (clazz.getSuperclass().getSuperclass() != null) ? COMMA : "\n"));
            }
            clazz = clazz.getSuperclass();
            if (clazz != null && clazz.getSuperclass() != null) {
                result.append(String.format("%s\"%s\": ", tabs(tabs), clazz.getSimpleName()));
                result.append(getFields(obj, clazz, tabs));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        result.append(String.format(END,tabs(--tabs)));
        return result;
    }

    void addType(Class clazz, JSONFormatter format) {
        types.put(clazz, format);
    }

    public static boolean isPrimitiveOrWrapper(Class<?> type) {
        return (type.isPrimitive() && type != void.class) ||
                type == Double.class || type == Float.class || type == Long.class ||
                type == Integer.class || type == Short.class || type == Character.class ||
                type == Byte.class || type == Boolean.class;
    }
}
