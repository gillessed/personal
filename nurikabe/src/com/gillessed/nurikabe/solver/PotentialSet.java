package com.gillessed.nurikabe.solver;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import com.gillessed.nurikabe.model.State;

public class PotentialSet {
    private final Set<Point> points;
    private final int size;
    private final boolean connected;

    public PotentialSet(int size, Set<Point> points) {
        this.size = size;
        this.points = new HashSet<>();
        this.points.addAll(points);

        Stack<Point> frontier = new Stack<>();
        Set<Point> connectivity = new HashSet<>();
        for(Point point : points) {
            frontier.push(point);
            break;
        }
        while(!frontier.isEmpty()) {
            Point point = frontier.pop();
            connectivity.add(point);
            for(Point neighbour : point.getNeighbours()) {
                if(!connectivity.contains(neighbour) && points.contains(neighbour)) {
                    frontier.push(neighbour);
                }
                if(points.contains(neighbour)) {
                    connectivity.add(neighbour);
                }
            }
        }
        connected = connectivity.size() == points.size();
    }

    public int getSize() {
        return size;
    }

    public Set<Point> getPoints() {
        return points;
    }

    public Set<Point> getEmptyBorder() {
        Set<Point> border = new HashSet<>();
        for(Point point : points) {
            if(point.getState() == State.EMPTY && !border.contains(border) && !points.contains(border)) {
                border.add(point);
            }
        }
        return border;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof PotentialSet)) {
            return false;
        }
        PotentialSet set = (PotentialSet)obj;
        return points == set.getPoints() && size == set.getSize();
    }

    @Override
    public int hashCode() {
        int result = 17;
        result += size * 31;
        result += points.hashCode() * 31;
        return result;
    }

    public boolean isConnected() {
        return connected;
    }
}
