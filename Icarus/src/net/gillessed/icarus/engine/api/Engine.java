package net.gillessed.icarus.engine.api;

import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * An engine is a class that will run a set of tasks in order to accomplish
 * some goal.
 *
 * @author gcole
 */
public interface Engine<T> {
    public T run() throws InterruptedException, ExecutionException;
    public Map<Task<?>, Double> getProgress();
}
