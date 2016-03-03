package com.gillessed.nurikabe.solver;

import java.util.HashSet;
import java.util.Set;

import com.gillessed.nurikabe.model.State;

public class Pool {
    private Set<Point> points;

    public Pool(Point point) {
        points = new HashSet<>();
        points.add(point);
    }

    public Pool(Set<Point> points) {
        this.points = new HashSet<>();
        this.points.addAll(points);
    }

    public void add(Point point) {
        points.add(point);
    }

    public Set<Point> getPoints() {
        return points;
    }

    private Set<Point> getBorder() {
        Set<Point> border = new HashSet<>();
        for(Point point : points) {
            for(Point neighbour : point.getNeighbours()) {
                if(!points.contains(neighbour)) {
                    border.add(neighbour);
                }
            }
        }
        return border;
    }

    public boolean expand(PoolManager poolManager) {
        Point borderPoint = null;
        int emptyBorder = 0;
        for(Point point : getBorder()) {
            if(point.getState() == State.EMPTY) {
                borderPoint = point;
                emptyBorder++;
            }
        }
        if(emptyBorder == 1) {
            poolManager.setPool(borderPoint);
            return true;
        } else {
            return false;
        }
    }
}
