package com.sensetrigger.jsonparser;

import java.util.Calendar;

public class JSONCalendar extends JSONDate {
    @Override
    public String convert(String name, Object obj, int tabs) {
        return super.convert(name, ((Calendar) obj).getTime(), tabs);
    }
}
