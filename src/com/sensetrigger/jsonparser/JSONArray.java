package com.sensetrigger.jsonparser;

import java.lang.reflect.Array;

public class JSONArray extends JSONFormatter {
    public static final String FIELD = "%s\"%s\"";

    @Override
    public String convert(String name, Object obj, int tabs) {
        StringBuilder result = new StringBuilder();
        result.append(String.format("%s\"%s\": [\n",tabs(tabs), name));
        tabs++;
        for (int i = 0; i < Array.getLength(obj); i++)
        {
            result.append(String.format(FIELD, tabs(tabs), Array.get(obj, i).toString()));
            result.append(((i < Array.getLength(obj) - 1)) ? Parser.COMMA : "\n");
        }
        tabs--;
        result.append(String.format("%s]",tabs(tabs)));
        return result.toString();
    }
}
