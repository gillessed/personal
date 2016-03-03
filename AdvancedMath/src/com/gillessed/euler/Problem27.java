package com.gillessed.euler;

import java.util.HashMap;
import java.util.Map;

import com.gillessed.euler.utils.Utils;

public class Problem27 implements Problem<Long> {

    private static class Key {
        public long a;
        public long b;
        public Key(long a, long b) {
            this.a = a;
            this.b = b;
        }
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + (int) (a ^ (a >>> 32));
            result = prime * result + (int) (b ^ (b >>> 32));
            return result;
        }
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Key other = (Key) obj;
            if (a != other.a)
                return false;
            if (b != other.b)
                return false;
            return true;
        }
        @Override
        public String toString() {
            return String.format("[%d, %d]", a, b);
        }
    }

    private static final long X = 1000;

    @Override
    public Long evaluate() {
        Map<Key, Integer> lengths = new HashMap<>();
        for(long a = 1 - X; a < X; a++) {
            for(long b = 1 - X; b < X; b++) {
                if(!Utils.isPrime(Math.abs(b))) {
                    continue;
                }
                int n = 0;
                while(Utils.isPrime(n * n + n * a + b)) {
                    n++;
                }
                lengths.put(new Key(a, b), n - 1);
            }
        }

        Key key = null;
        int longest = 0;
        for(Map.Entry<Key, Integer> entry : lengths.entrySet()) {
            if(entry.getValue() > longest) {
                longest = entry.getValue();
                key = entry.getKey();
            }
        }

        return key.a * key.b;
    }
}
