package com.sensetrigger.jsonparser;

public abstract class JSONFormatter<T> {
    public static StringBuilder tabs(int tabs) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < tabs; i++)
            result.append("\t");
        return result;
    }

    public abstract String convert(String name, Object obj, int tabs);
}
