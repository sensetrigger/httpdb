package com.sensetrigger.jsonparser;

public class JSONString extends JSONFormatter {
    @Override
    public String convert(String name, Object obj, int tabs) {
        return String.format("%s\"%s\": \"%s\"", tabs(tabs), name, (String) obj);
    }
}
