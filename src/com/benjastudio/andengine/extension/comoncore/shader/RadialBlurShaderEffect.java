package com.benjastudio.andengine.extension.comoncore.shader;

public class RadialBlurShaderEffect extends ShaderEffect {

	public float duration;
	public float distance;
	public float strength;
	public boolean isActive;

	public RadialBlurShaderEffect() {
		super(0, 0, 0);
		duration = 0;
		isActive = false;
		distance = 0.2f;
		strength = 1.25f;
	}

}
