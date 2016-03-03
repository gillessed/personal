package com.gillessed.nurikabe.solver;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.gillessed.nurikabe.model.Nurikabe;
import com.gillessed.nurikabe.model.State;

public class Solver {
    private final Set<Island> islands;
    private final Map<Point, Island> islandMap;
    private final PoolManager poolManager;
    private final Nurikabe nurikabe;

    public Solver(Nurikabe nurikabe) {
        this.nurikabe = nurikabe;
        Point.createPointPool(nurikabe.getSize());
        islands = new HashSet<>();
        islandMap = new HashMap<>();
        poolManager = new PoolManager();
        for(int i = 0; i < nurikabe.getSize(); i++) {
            for(int j = 0; j < nurikabe.getSize(); j++) {
                int state = nurikabe.getState(i, j);
                if(state > 0) {
                    Island island = new Island(state, Point.getPoint(i, j));
                    islands.add(island);
                    islandMap.put(Point.getPoint(i, j), island);
                    Point.getPoint(i, j).setNumber(state);
                }
            }
        }
    }

    public void solve() {
        while(!Point.solved()) {
            iterate();
        }
    }

    public void iterate() {
        for(Island island : islands) {
            island.update();
        }

        for(Island island : islands) {
            if(island.complete(poolManager, islandMap)) {
                System.out.println("Completed an island");
                return;
            }
        }

        for(Island island : islands) {
            if(island.expand(islandMap)) {
                System.out.println("Expanded an island");
                return;
            }
        }

        if(poolManager.expand()) {
            System.out.println("Expanded a pool");
            return;
        }

        Set<Island> islandNeighbours = new HashSet<>();
        for(Point point : Point.getPoints()) {
            if(point.getState() == State.EMPTY) {
                islandNeighbours.clear();
                for(Point neighbour : point.getNeighbours()) {
                    if(neighbour.getState() == State.ISLAND) {
                        islandNeighbours.add(islandMap.get(neighbour));
                    }
                }
                if(islandNeighbours.size() >= 2) {
                    poolManager.setPool(point);
                    System.out.println("Found pool between islands.");
                    return;
                }
            }
        }

        System.out.println(toString());

        throw new RuntimeException("Found no move to proceed any further.");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int y = 0; y < nurikabe.getSize(); y++) {
            for(int x = 0; x < nurikabe.getSize(); x++) {
                Point point = Point.getPoint(x, y);
                if(point.getNumber() > 0) {
                    sb.append(point.getNumber());
                } else {
                    State state = point.getState();
                    if(state == State.ISLAND) {
                        sb.append("O");
                    } else if(state == State.POOL) {
                        sb.append("*");
                    } else {
                        sb.append(".");
                    }

                }
            }
            sb.append("\n");
        }
        sb.append("\n");
        return sb.toString();
    }
}
