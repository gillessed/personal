package net.gillessed.icarus.event;

public interface ProgressListener {
	public void progressChangeEventPerformed(ProgressChangeEvent e);
	public void setHolder(Object holder);
	public Object getHolder();
}
