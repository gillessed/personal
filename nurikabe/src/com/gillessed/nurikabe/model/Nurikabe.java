package com.gillessed.nurikabe.model;

public class Nurikabe {
    private int[][] state;
    private int size;

    public Nurikabe(int size, int[][] state) {
        this.size = size;
        this.state = state;
    }

    public int getState(int x, int y) {
        return state[x][y];
    }

    public void setState(int value, int x, int y) {
        state[x][y] = value;
    }

    public int getSize() {
        return size;
    }
}
