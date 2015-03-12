package net.gillessed.icarus.engine;

import java.util.Map;

import javax.swing.JProgressBar;

import net.gillessed.icarus.engine.api.Task;

public class ProgressBarUpdater implements ProgressUpdater {

    private final JProgressBar progressBar;

    public ProgressBarUpdater(JProgressBar progressBar) {
        this.progressBar = progressBar;
        progressBar.setValue(0);
        progressBar.setString("Idle");
    }

    @Override
    public void updateProgress(Map<Task<?>, Double> progressMap) {
        double progress = 0;
        for(Double d : progressMap.values()) {
            progress += d;
        }
        progress /= progressMap.size();
        progressBar.setValue((int)(100 * progress));
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<Task<?>, Double> entry : progressMap.entrySet()) {
            sb.append("[");
            sb.append(entry.getKey().getName());
            sb.append(" : ");
            sb.append((int)(100 * entry.getValue()));
            sb.append("] ");
        }
        progressBar.setString(sb.toString());
    }
}
