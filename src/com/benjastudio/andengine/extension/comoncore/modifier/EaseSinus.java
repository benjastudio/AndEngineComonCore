package com.benjastudio.andengine.extension.comoncore.modifier;

import org.andengine.util.modifier.ease.IEaseFunction;

public class EaseSinus implements IEaseFunction {

	private static EaseSinus INSTANCE;

	private EaseSinus() {

	}

	public static EaseSinus getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new EaseSinus();
		}
		return INSTANCE;
	}

	@Override
	public float getPercentage(final float pSecondsElapsed,
			final float pDuration) {
		return (float) Math.sin(2 * Math.PI * pSecondsElapsed / pDuration);
	}
}
