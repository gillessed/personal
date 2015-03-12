package net.gillessed.icarus.engine.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class stores results from previous tasks
 * to pass them to future tasks.
 *
 * @author gcole
 */
public final class ResultMap {

    private Map<Class<? extends Task<?>>, List<Object>> internalMap;

    public ResultMap() {
        internalMap = new HashMap<>();
    }

    public synchronized void put(Class<? extends Task<?>> key, Object value) {
        if(!internalMap.containsKey(key)) {
            internalMap.put(key, new ArrayList<>());
        }
        internalMap.get(key).add(value);
    }

    @SuppressWarnings("unchecked")
    public synchronized <T> List<T> get(Class<? extends Task<T>> key) {
        return (List<T>)internalMap.get(key);
    }
}
