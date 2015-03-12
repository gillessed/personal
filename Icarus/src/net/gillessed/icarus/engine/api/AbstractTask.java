package net.gillessed.icarus.engine.api;

import java.util.List;

import com.google.common.base.Preconditions;

/**
 * Default implementation of {@link Task}.
 *
 * @author gcole
 */
public abstract class AbstractTask<T> implements Task<T> {

    private final String name;
    private long maxProgress;
    private ResultMap resultMap;

    private long progress;

    /**
     * @param name A unique human-readable identifier for the task.
     * @param maxProgress The number of units of work this task does
     * that represents completion.
     */
    public AbstractTask(String name) {
        this.name = name;
        progress = 0;
    }

    protected void setMaxProgress(long maxProgress) {
        this.maxProgress = maxProgress;
    }

    protected void incrementProgress(long units) {
        Preconditions.checkState(maxProgress != 0, "Forget to set maxProgress at start of task %s.", getName());
        progress += units;
    }

    /**
     * Get the results of all tasks of type {@code task}
     */
    protected <E> List<E> getResultsForTask(Class<? extends Task<E>> task) {
        return resultMap.get(task);
    }

    /**
     * Get the result of a task of type {@code task}.
     * There must only be a single result for that task type.
     */
    protected <E> E getSingleResultForTask(Class<? extends Task<E>> task) {
        List<E> results = resultMap.get(task);
        Preconditions.checkState(results.size() == 1);
        return results.get(0);
    }

    @Override
    public double getProgress() {
        return (double)progress / maxProgress;
    }

    @Override
    public void setResultMap(ResultMap map) {
        resultMap = map;
    }

    @Override
    public String getName() {
        return name;
    }
}
