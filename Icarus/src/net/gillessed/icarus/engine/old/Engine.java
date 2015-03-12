package net.gillessed.icarus.engine.old;

import net.gillessed.icarus.event.ProgressChangeEvent;

/**
 * API for running a chain of multiple threads.
 */
public interface Engine {
	public void run();
	public EngineThread getCurrentThread();
	public void fireProgressChangeEvent(ProgressChangeEvent e);
	public void setThreadState(String threadState);
	public String getThreadState();
	public int getProgress();
	public boolean isRunning();
}
