package net.gillessed.icarus.engine.old;

import net.gillessed.icarus.event.ProgressChangeEvent;
import net.gillessed.logging.Logger;
import net.gillessed.utils.timer.Timer;


public abstract class AbstractEngine implements Engine {

	/**
	 * The thing we notify when the progress of the thread changes.
	 */
	protected EngineMonitor engineMonitor;
	
	/**
	 * The list of threads to be run.
	 */
	protected EngineThread initialThread;
	
	/**
	 * Ths thread that is currently running.
	 */
	private EngineThread currentThread;
	
	/**
	 * Tells us if a thread is currently running or not.
	 */
	private boolean isRunning;
	
	/**
	 * The string describing the initial state of the engine.
	 */
	protected String initialState;
	
	/**
	 * The description of the current state of the engine.
	 */
	private String threadState;
	
	private Timer timer;
	
	public AbstractEngine(EngineMonitor engineMonitor, String initialState) {
		this.engineMonitor = engineMonitor;
		this.initialState = initialState;
		setThreadState(initialState);
		currentThread = null;
		isRunning = false;
	}
	
	@Override
	public void run() {
		if(initialThread == null) {
			throw new IllegalArgumentException("You need to specify the engine's initial thread before running it.");
		}
		currentThread = initialThread;
		timer = new Timer();
		Logger.getLogger().dev(Thread.currentThread().getName() + " :: Engine Starting");
		startThread(currentThread);
		isRunning = true;
	}
	public void startThread(EngineThread et) {
		et.restart(currentThread.getProgressTotal());
		et.start();
	}
	@Override
	public EngineThread getCurrentThread() {
		return currentThread;
	}
	@Override
	public int getProgress() {
		EngineThread t = getCurrentThread();
		return t == null ? 0 : t.getProgress();
	}
	@Override
	public void fireProgressChangeEvent(ProgressChangeEvent e) {
		if(engineMonitor != null) {
			engineMonitor.fireProgressChangeEvent(e);
		}
		if(e.isTaskDone()) {
			Logger.getLogger().dev(Thread.currentThread().getName() + " :: Engine Thread ( " + currentThread.getClass().getSimpleName() + ") completed in " + timer.elapsedTimeSeconds() + "s.");
			timer.restart();
			currentThread = currentThread.getNextThread();
			if(currentThread == null) {
				isRunning = false;
				Logger.getLogger().dev(Thread.currentThread().getName() + " :: Engine Stopped");
				if(engineMonitor != null) {
					engineMonitor.fireProgressChangeEvent(new ProgressChangeEvent(currentThread, 100, false, true));
				}
			} else {
				System.gc();
				startThread(currentThread);
			}
		}
	}
	@Override
	public void setThreadState(String threadState) {
		this.threadState = threadState;
		if(engineMonitor != null) {
			engineMonitor.setThreadState(threadState);
		}
	}	
	@Override
	public String getThreadState() {
		return threadState;
	}
	@Override
	public boolean isRunning() {
		return isRunning;
	}
}
