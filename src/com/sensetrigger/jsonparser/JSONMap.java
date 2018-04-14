package com.sensetrigger.jsonparser;

import java.util.Map;

public class JSONMap extends JSONArray{
    @Override
    public String convert(String name, Object obj, int tabs) {
        return super.convert(name, ((Map) obj).entrySet().toArray(), tabs);
    }
}
