package net.gillessed.icarus.engine.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

/**
 * Implementation of the {@link Engine} class. It is a framework for running
 * a series of connected concurrent tasks.
 *
 * @author gcole
 */
public class AbstractEngine<T> implements Engine<T> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractEngine.class.getSimpleName());

    private final ExecutorService executorService;
    private final List<Set<Task<?>>> tasks;
    private final ResultMap resultMap;

    private int index;
    private Set<Task<?>> currentTaskSet;
    private Task<T> finalTask;

    public AbstractEngine() {
        this(Executors.newCachedThreadPool());
    }

    public AbstractEngine(ExecutorService executorService) {
        this.executorService = executorService;
        tasks = new ArrayList<>();
        resultMap = new ResultMap();
        index = 0;
    }

    /**
     * Add another task to the current task set.
     */
    public void addTask(Task<?> task) {
        Preconditions.checkNotNull(task);
        if(tasks.size() <= index) {
            tasks.add(new HashSet<Task<?>>());
        }
        tasks.get(index).add(task);
    }

    /**
     * Finishes off the current task set. Everything in the current task set
     * must be finished before the next task set begins.
     */
    public void addBarrier() {
        index++;
    }

    /**
     * Adds a task to the task set and then immediately
     * finishes off that task set.
     */
    public void addTaskAndBarrier(Task<?> task) {
        addTask(task);
        addBarrier();
    }

    /**
     * Set the task that will be executed last.
     * @param task
     */
    public void addFinalTask(Task<T> task) {
        Preconditions.checkNotNull(task);
        this.finalTask = task;
    }

    @Override
    public T run() throws InterruptedException, ExecutionException {
        for(Set<Task<?>> taskSet : tasks) {
        	long currentTime = System.nanoTime();
            currentTaskSet = taskSet;
            Map<Future<?>, Task<?>> futures = new HashMap<>();
            for(Task<?> task : taskSet) {
                task.setResultMap(resultMap);
                logger.info("Starting task " + task.getName());
                Future<?> future = executorService.submit(task);
                futures.put(future, task);
            }
            for(Future<?> future : futures.keySet()) {
                Object obj = future.get();
                logger.info("Finished task " + futures.get(future).getName());
                @SuppressWarnings("unchecked")
                Class<? extends Task<?>> taskClass = (Class<? extends Task<?>>)futures.get(future).getClass();
                resultMap.put(taskClass, obj);
            }
            long timeDiff = System.nanoTime() - currentTime;
            long seconds = timeDiff / 1000000000l;
            long milliSeconds = (timeDiff - seconds) / 1000000l;
            logger.info("Task set took " + seconds + "." + milliSeconds + " seconds.");
        }
        finalTask.setResultMap(resultMap);
        Future<T> finalTaskResult = executorService.submit(finalTask);
        return finalTaskResult.get();
    }

    @Override
    public Map<Task<?>, Double> getProgress() {
        Map<Task<?>, Double> progress = new HashMap<>();
        for(Task<?> task : currentTaskSet) {
            progress.put(task, task.getProgress());
        }
        return progress;
    }
}
