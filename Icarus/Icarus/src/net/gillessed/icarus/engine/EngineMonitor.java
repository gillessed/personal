package net.gillessed.icarus.engine;

import net.gillessed.icarus.FlameModel;
import net.gillessed.icarus.event.ProgressChangeEvent;

public interface EngineMonitor {
	public void fireProgressChangeEvent(ProgressChangeEvent e);
	public void setThreadState(String threadState);
	public FlameModel getFlameModel();
}
