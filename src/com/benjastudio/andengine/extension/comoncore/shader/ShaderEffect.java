package com.benjastudio.andengine.extension.comoncore.shader;

public abstract class ShaderEffect {
	public float x, y, t;

	public ShaderEffect(final float pX, final float pY, final float pT) {
		t = pT;
		x = pX;
		y = pY;
	}
}
