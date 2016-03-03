package com.gillessed.nurikabe.solver;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import com.gillessed.nurikabe.model.State;

public class Island {
    private final int size;
    private final Set<Point> points;
    private final Set<Point> border;
    private final Set<Point> potentialPoints;
    private boolean completed;

    public Island(int size, Point startingPoint) {
        this.size = size;
        points = new HashSet<>();
        border = new HashSet<>();
        potentialPoints = new HashSet<>();
        points.add(startingPoint);
        startingPoint.setState(State.ISLAND);
        completed = false;
    }

    public void update() {
        updateBorder();

        potentialPoints.clear();
        int currentSize = points.size();
        Set<PotentialSet> seen = new HashSet<>();
        Stack<PotentialSet> frontier = new Stack<>();
        frontier.add(new PotentialSet(currentSize, points));
        while(!frontier.isEmpty()) {
            PotentialSet pot = frontier.pop();
            seen.add(pot);
            if(pot.getSize() == size) {
                potentialPoints.addAll(pot.getPoints());
            } else {
                for(Point point : pot.getEmptyBorder()) {
                    Set<Point> newPotSet = new HashSet<>();
                    newPotSet.addAll(pot.getPoints());
                    newPotSet.add(point);
                    PotentialSet newPot = new PotentialSet(pot.getSize() + 1, newPotSet);
                    if(!seen.contains(newPot) && newPot.isConnected()) {
                        frontier.add(newPot);
                    }
                }
            }
        }
    }

    private void updateBorder() {
        border.clear();
        for(Point point : points) {
            for(Point neighbour : point.getNeighbours()) {
                if(!points.contains(neighbour)) {
                    border.add(neighbour);
                }
            }
        }
    }

    public boolean expand(Map<Point, Island> islandMap) {
        if(size == points.size()) {
            return false;
        }
        Point borderPoint = null;
        int emptyBorder = 0;
        for(Point point : border) {
            if(point.getState() == State.EMPTY) {
                borderPoint = point;
                emptyBorder++;
            }
        }
        if(emptyBorder == 1) {
            points.add(borderPoint);
            islandMap.put(borderPoint, this);
            borderPoint.setState(State.ISLAND);
            return true;
        } else {
            return false;
        }
    }

    public boolean complete(PoolManager poolManager, Map<Point, Island> islandMap) {
        if(completed) {
            return false;
        }
        if(potentialPoints.size() == size) {
            points.addAll(potentialPoints);
            for(Point point : points) {
                islandMap.put(point, this);
                point.setState(State.ISLAND);
            }
            updateBorder();
        }
        if(points.size() == size) {
            for(Point point : border) {
                poolManager.setPool(point);
            }
            completed = true;
            return true;
        } else {
            return false;
        }
    }
}
