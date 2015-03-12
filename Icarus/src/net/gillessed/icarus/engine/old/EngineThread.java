package net.gillessed.icarus.engine.old;

import net.gillessed.icarus.event.ProgressChangeEvent;

/**
 * This is the API for the engine threads that the engine runs.
 */
public abstract class EngineThread extends Thread {
	protected final Engine engine;
	protected double progressTotal;
	protected double localProgress;
	protected String threadState;
	
	private final EngineThread nextThread;
	
	public EngineThread(Engine engine, EngineThread nextThread, String threadState) {
		this.engine = engine;
		this.nextThread = nextThread;
		this.threadState = threadState;
		progressTotal = 0;
	}

	public EngineThread getNextThread() {
		return nextThread;
	}
	
	public int getProgress() {
		return (int)(localProgress / progressTotal * 100);
	}
	
	protected void augmentProgress() {
		localProgress++;
		engine.fireProgressChangeEvent(new ProgressChangeEvent(this, (int)(localProgress / progressTotal * 100), false, false));
	}
	
	protected void restart(double progressTotal) {
		localProgress = 0;
		this.progressTotal = getProgressTotal();
		engine.fireProgressChangeEvent(new ProgressChangeEvent(this, 0, false, false));
		engine.setThreadState(getThreadState());
	}
	
	public String getThreadState() {
		return threadState;
	}
	
	@Override
	public abstract void run();
	
	public abstract double getProgressTotal();
}
