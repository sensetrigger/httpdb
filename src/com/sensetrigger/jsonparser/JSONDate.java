package com.sensetrigger.jsonparser;

import java.text.SimpleDateFormat;

public class JSONDate extends JSONFormatter {
    @Override
    public String convert(String name, Object obj, int tabs) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

        return String.format(Parser.FIELD, tabs(tabs), name, sdf.format(obj));
    }
}
