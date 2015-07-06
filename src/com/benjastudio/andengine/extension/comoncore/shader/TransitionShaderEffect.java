package com.benjastudio.andengine.extension.comoncore.shader;

public class TransitionShaderEffect extends ShaderEffect {

	public float duration;
	public boolean isActive;

	public TransitionShaderEffect() {
		super(0, 0, 0);
		duration = 0;
		isActive = false;
	}

}
