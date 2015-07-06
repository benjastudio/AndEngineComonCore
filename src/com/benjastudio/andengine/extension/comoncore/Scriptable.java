package com.benjastudio.andengine.extension.comoncore;

import org.andengine.engine.handler.IUpdateHandler;

public interface Scriptable {
	public void runScript(final String basePath, final String scriptName);

	public void registerUpdateHandler(final IUpdateHandler pUpdateHandler);
}
