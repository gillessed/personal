package com.gillessed.euler;

import java.util.Collections;
import java.util.List;

import com.gillessed.euler.utils.Utils;

public class Problem24 implements Problem<String> {

    @Override
    public String evaluate() {
        List<String> perms = Utils.permutations("0123456789");
        Collections.sort(perms);
        return perms.get(999999);
    }
}
