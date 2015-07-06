package com.benjastudio.andengine.extension.comoncore.modifier;

import org.andengine.util.modifier.ease.EaseElasticOut;
import org.andengine.util.modifier.ease.IEaseFunction;

public class EaseCustomElastic implements IEaseFunction {

	private static EaseCustomElastic INSTANCE;

	private EaseCustomElastic() {

	}

	public static EaseCustomElastic getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new EaseCustomElastic();
		}
		return INSTANCE;
	}

	@Override
	public float getPercentage(final float pSecondsElapsed,
			final float pDuration) {
		final float percentage = pSecondsElapsed / pDuration;
		return EaseElasticOut.getValue(pDuration - pSecondsElapsed, pDuration,
				percentage);
	}
}
