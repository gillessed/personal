package net.gillessed.icarus.engine;

import java.util.Map;

import net.gillessed.icarus.engine.api.Task;

public interface ProgressUpdater {
    void updateProgress(Map<Task<?>, Double> progressMap);
    void stop();
}
