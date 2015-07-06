package com.benjastudio.andengine.extension.comoncore.entity.effect;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.opengl.GLES20;

public class RectangleEffect extends Rectangle {

	public enum Effect {
		pause, soft_bright, bright, win, dark_red
	}

	public RectangleEffect(float pX, float pY, float pWidth, float pHeight,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pWidth, pHeight, pVertexBufferObjectManager);
	}

	public RectangleEffect(float pX, float pY, float pWidth, float pHeight,
			Effect effect, VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pWidth, pHeight, pVertexBufferObjectManager);
		if (effect != null)
			setEffect(effect);
	}

	public void setEffect(Effect effect) {
		switch (effect) {
		case pause:
			setColor(0.4f, 0.5f, 0.2f);
			setBlendFunction(GLES20.GL_ONE_MINUS_SRC_COLOR, GLES20.GL_DST_COLOR);
			break;
		case soft_bright:
			setColor(0.12f, 0.12f, 0.05f);
			setBlendFunction(GLES20.GL_DST_COLOR, GLES20.GL_ONE);
			break;
		case bright:
			setColor(0.3f, 0.3f, 0.1f);
			setBlendFunction(GLES20.GL_DST_COLOR, GLES20.GL_ONE);
			break;
		case win:
			setColor(0.6f, 0.7f, 0.6f);
			setBlendFunction(GLES20.GL_ONE_MINUS_CONSTANT_COLOR,
					GLES20.GL_DST_COLOR);
			break;
		case dark_red:
			setColor(0.5f, 0.3f, 0.3f);
			setBlendFunction(GLES20.GL_ZERO, GLES20.GL_SRC_COLOR);
			break;
		default:
			break;
		}
	}
}
