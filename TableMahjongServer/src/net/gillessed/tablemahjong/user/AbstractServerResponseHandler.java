package net.gillessed.tablemahjong.user;

import net.gillessed.tablemahjong.server.ResponseHandler;
import net.gillessed.tablemahjong.threadspace.MainThreadSpace;

public abstract class AbstractServerResponseHandler implements ResponseHandler {

	protected final MainThreadSpace mainThreadSpace;
	protected final User user;

	public AbstractServerResponseHandler(MainThreadSpace mainThreadSpace, User user) {
		this.mainThreadSpace = mainThreadSpace;
		this.user = user;
	}
}
