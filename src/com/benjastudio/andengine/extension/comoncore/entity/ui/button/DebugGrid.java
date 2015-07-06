package com.benjastudio.andengine.extension.comoncore.entity.ui.button;

import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Line;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class DebugGrid extends Entity {

	public DebugGrid(VertexBufferObjectManager vbom, int width, int height,
			int tileSize) {
		int cols = width / tileSize;
		int rows = height / tileSize;

		for (int i = 0; i < cols; i++) {
			attachChild(new Line(i * tileSize, 0, i * tileSize, height, 1, vbom));
		}
		for (int i = 0; i < rows; i++) {
			attachChild(new Line(0, i * tileSize, width, i * tileSize, 1, vbom));
		}
	}
}
