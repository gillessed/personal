package com.gillessed.euler;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class Problem26 implements Problem<Long> {
    @Override
    public Long evaluate() {
        Map<Long, String> cycles = new HashMap<>();
        for(long i = 2; i < 1000; i++) {
            BigDecimal denomenator = BigDecimal.valueOf(i);
            BigDecimal fraction = BigDecimal.ONE.divide(denomenator, 10000, RoundingMode.HALF_EVEN);
            String fractionString = fraction.toPlainString().substring(2);
            String cycle = findCycle(fractionString);
            cycles.put(i, cycle);
        }
        long key = -1;
        int longest = 0;
        for(Map.Entry<Long, String> entry : cycles.entrySet()) {
            if(entry.getValue().length() > longest) {
                longest = entry.getValue().length();
                key = entry.getKey();
            }
        }
        return key;
    }

    private String findCycle(String str) {
        int startIndex = 0;
        while(startIndex < 5) {
            int length = 1;
            while(length < 1000) {
                String pattern = str.substring(startIndex, startIndex + length);
                boolean match = true;
                for(int i = 1; i < 5; i++) {
                    if(!str.substring(startIndex + i * length, startIndex + (i + 1) * length).equals(pattern)) {
                        match = false;
                    }
                }
                if(match == true) {
                    return pattern;
                }
                length++;
            }
            startIndex++;
        }
        return "";
    }
}
