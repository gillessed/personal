package net.gillessed.icarus.event;

public abstract class AbstractProgressListener implements ProgressListener {
	private Object holder;
	public AbstractProgressListener() {
		holder = null;
	}
	public abstract void progressChangeEventPerformed(ProgressChangeEvent e);
	public void setHolder(Object holder) {
		this.holder = holder;
	}
	public Object getHolder() {
		return holder;
	}
}
