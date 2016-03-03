package com.gillessed.euler;

public class Problem28 implements Problem<Long> {

    private static final long SIZE = 1001;

    @Override
    public Long evaluate() {
        long sum = 1;
        for(long r = 1; r <= (SIZE - 1) / 2; r++) {
            long x = r * 2 + 1;
            sum += x * x * 4 - (x - 1) * 6;
        }

        return sum;
    }

}
