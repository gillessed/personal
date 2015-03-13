package net.gillessed.icarus.engine;

import java.util.Map;

import javax.swing.JProgressBar;

import net.gillessed.icarus.engine.api.Task;

public class ProgressBarUpdater implements ProgressUpdater {

    private final JProgressBar progressBar;
    private boolean stopped;

    public ProgressBarUpdater(JProgressBar progressBar) {
        this.progressBar = progressBar;
        progressBar.setValue(0);
        progressBar.setString("Idle");
        stopped = false;
    }

    @Override
    public synchronized void updateProgress(Map<Task<?>, Double> progressMap) {
    	if(!stopped) {
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

	@Override
	public synchronized void stop() {
		progressBar.setString("Finished");
		progressBar.setValue(100);
		stopped = true;
	}
}
