package net.gillessed.icarus.engine.api;

import java.util.concurrent.Callable;


/**
 * A task is something that does a trackable amount of work.
 *
 * @author gcole
 */
public interface Task<T> extends Callable<T> {

    /**
     * Figure out how far along to completion this task is.
     * 0 is the starting state. 1 is completed.
     */
    double getProgress();

    /**
     * Hook to set the result map, so this thread can access the values
     * computed by previous threads.
     */
    void setResultMap(ResultMap map);

    /**
     * Returns a human readable indentifier.
     */
    String getName();
}
