package com.gillessed.nurikabe.solver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.gillessed.nurikabe.model.State;

public class Point {
    private static final List<Point> points = new ArrayList<>();
    private static int size;
    public static void createPointPool(int newSize) {
        size = newSize;
        points.clear();
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                points.add(new Point(i, j));
            }
        }
        for(Point point : points) {
            point.setupNeighbours();
        }
    }

    public static Point getPoint(int x, int y) {
        return points.get(x * size + y);
    }

    public static boolean solved() {
        for(Point point : points) {
            if(point.getState() == State.EMPTY) {
                return false;
            }
        }
        return true;
    }

    public static List<Point> getPoints() {
        return points;
    }

    private final int x;
    private final int y;
    private State state;
    private final Set<Point> neighbours;
    private int number;

    private Point(int x, int y) {
        this.x = x;
        this.y = y;
        state = State.EMPTY;
        neighbours = new HashSet<Point>();
        number = -1;
    }

    private void setupNeighbours() {
        if(x > 0) {
            neighbours.add(Point.getPoint(x - 1, y));
        }
        if(x < size - 1) {
            neighbours.add(Point.getPoint(x + 1, y));
        }
        if(y > 0) {
            neighbours.add(Point.getPoint(x, y - 1));
        }
        if(y < size - 1) {
            neighbours.add(Point.getPoint(x, y + 1));
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Set<Point> getNeighbours() {
        return neighbours;
    }

    public boolean isNeighbour(Point point) {
       return neighbours.contains(point);
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Point[" + x + ", " + y + ", " + state + "]";
    }
}
