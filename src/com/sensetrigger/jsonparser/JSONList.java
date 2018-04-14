package com.sensetrigger.jsonparser;

import java.util.List;

public class JSONList extends JSONArray {
    @Override
    public String convert(String name, Object obj, int tabs) {
        return super.convert(name, ((List) obj).toArray(), tabs);
    }
}
