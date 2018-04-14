package com.sensetrigger.jsonparser;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

public abstract class JSONObject extends JSONFormatter {
/*    @Override

    public String convert(String name, Object obj, int tabs) {
        Class clazz = obj.getClass();
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
                result.append(convert(obj, clazz, tabs));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        result.append(String.format(END,tabs(--tabs)));
        return result.toString();
    }*/
}
