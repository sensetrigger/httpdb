package com.sensetrigger.jsonparser;

import java.util.Set;

public class JSONSet extends JSONArray {
    @Override
    public String convert(String name, Object obj, int tabs) {
        return super.convert(name, ((Set) obj).toArray(), tabs);
    }
}
