package com.gillessed.nurikabe.cli;

import com.gillessed.nurikabe.model.Nurikabe;
import com.gillessed.nurikabe.solver.Solver;

public class Solve {
    public static void main(String args[]) {
        int[][] states = new int[][] {
            {1,0,2,0,0,3,0,0,0,2},
            {0,0,0,0,0,0,0,0,0,0},
            {3,0,2,0,0,0,0,0,2,0},
            {0,0,0,0,0,0,4,0,0,0},
            {0,0,0,2,0,3,0,2,0,0},
            {0,4,0,0,0,0,0,0,0,3},
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,3,0,0,5,0,0},
            {1,0,0,0,0,0,0,0,0,0},
            {0,0,1,0,0,0,0,0,0,0},
        };
        Nurikabe nurikabe = new Nurikabe(10, states);
        Solver solver = new Solver(nurikabe);
        solver.solve();
        System.out.println(solver.toString());
    }
}
