package com.gillessed.nurikabe.solver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gillessed.nurikabe.model.State;

public class PoolManager {
    private final Set<Pool> pools;
    private final Map<Point, Pool> poolMap;

    public PoolManager() {
        pools = new HashSet<>();
        poolMap = new HashMap<>();
    }

    public void setPool(Point point) {
        List<Pool> neighbouringPools = new ArrayList<>();
        for(Point neighbour : point.getNeighbours()) {
            if(neighbour.getState() == State.POOL) {
                neighbouringPools.add(poolMap.get(neighbour));
            }
        }
        point.setState(State.POOL);
        if(neighbouringPools.isEmpty()) {
            Pool pool = new Pool(point);
            poolMap.put(point, pool);
            pools.add(pool);
        } else if(neighbouringPools.size() == 1) {
            Pool neighbour = neighbouringPools.get(0);
            neighbour.add(point);
            poolMap.put(point, neighbour);
        } else {
            Set<Point> allPoints = new HashSet<>();
            for(Pool pool : neighbouringPools) {
                allPoints.addAll(pool.getPoints());
            }
            allPoints.add(point);
            Pool newPool = new Pool(allPoints);
            for(Point newPoint : allPoints) {
                poolMap.put(newPoint, newPool);
            }
            for(Pool pool : neighbouringPools) {
                pools.remove(pool);
            }
            pools.add(newPool);
        }
    }

    public boolean expand() {
        for(Pool pool : pools) {
            if(pool.expand(this)) {
                return true;
            }
        }
        return false;
    }
}
