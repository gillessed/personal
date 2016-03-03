package com.gillessed.euler;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

public class Problem29 implements Problem<Long> {

    private static final long X = 100;

    @Override
    public Long evaluate() {
        Set<BigInteger> powers = new HashSet<>();
        for(long a = 2; a <= X; a++) {
            for(long b = 2; b <= X; b++) {
                BigInteger _a = BigInteger.valueOf(a);
                powers.add(_a.pow((int)b));
            }
        }
        return (long)powers.size();
    }

}
