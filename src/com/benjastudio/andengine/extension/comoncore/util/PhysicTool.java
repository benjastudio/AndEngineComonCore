package com.benjastudio.andengine.extension.comoncore.util;

import com.badlogic.gdx.math.Vector2;

public class PhysicTool {

	static public final String TAG = "PhysicTool";

	static public void computeTrajectoryPoint(float tSeconds,
			final Vector2 position, final Vector2 velocity,
			final Vector2 gravity, Vector2 resultPosition) {
		resultPosition.x = position.x + 0.5f * gravity.x * tSeconds * tSeconds
				+ velocity.x * tSeconds;
		resultPosition.y = position.y + 0.5f * gravity.y * tSeconds * tSeconds
				+ velocity.y * tSeconds;
	}
}
