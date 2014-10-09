package net.gillessed.icarus.engine;

import java.util.UUID;

import net.gillessed.threadpool.Callback;
import net.gillessed.threadpool.HookedThread;
import net.gillessed.threadpool.ThreadPool;

public class FractalEngineThreadPool extends ThreadPool<Void>{

	private final FractalEngineThread fractalEngineThread;
	private final long quality;

	public FractalEngineThreadPool(long threadCount, Callback<Void> callback, FractalEngineThread fractalEngineThread, long quality) {
		super(threadCount, callback);
		this.fractalEngineThread = fractalEngineThread;
		this.quality = quality;
		makeThreads();
	}

	@Override
	protected Void getComputedValue() {
		return null;
	}

	@Override
	protected void makeThreads() {
		long amount = quality / threadCount;
		for(long i = 0; i < threadCount; i++) {
			String uuid = UUID.randomUUID().toString();
			HookedThread<Void> t = new FractalEngineHookedThread(this, uuid, fractalEngineThread, amount);
			threadMap.put(uuid, t);
			notificationMap.put(uuid, false);
		}
	}

}
