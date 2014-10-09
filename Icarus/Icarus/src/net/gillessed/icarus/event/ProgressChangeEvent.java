package net.gillessed.icarus.event;

public class ProgressChangeEvent {
	private final int progress;
	private final boolean engineDone;
	private final Object source;
	private final boolean taskDone;
	public ProgressChangeEvent(Object source, int progress, boolean taskDone, boolean engineDone) {
		this.source = source;
		this.taskDone = taskDone;
		this.engineDone = engineDone;
		this.progress = progress;
	}
	public int getProgress() {
		return progress;
	}
	public boolean isEngineDone() {
		return engineDone;
	}
	public Object getSource() {
		return source;
	}
	public String toString() {
		return "ProgressEvent[progress=" + progress + "; taskDone = " + taskDone + "; engineDone=" + engineDone + "]";
	}
	public boolean isTaskDone() {
		return taskDone;
	}
}
