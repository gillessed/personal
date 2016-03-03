package com.gillessed.euler;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Problem19 implements Problem<Long> {

    @Override
    public Long evaluate() {
        Calendar calendar = GregorianCalendar.getInstance();
        long count = 0;
        for(int year = 1901; year <= 2000; year++) {
            for(int month = Calendar.JANUARY; month <= Calendar.DECEMBER; month++) {
                calendar.set(year, month, 0);
                if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    count++;
                }
            }
        }
        return count;
    }
}
