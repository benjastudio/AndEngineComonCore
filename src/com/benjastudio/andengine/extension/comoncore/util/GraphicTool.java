package com.benjastudio.andengine.extension.comoncore.util;

import java.util.List;

import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Vector2;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class GraphicTool {

	public static Entity getDrawnRowMajoredMesh(float[] points,
			VertexBufferObjectManager vbom) {
		org.andengine.entity.Entity layer = new org.andengine.entity.Entity(0,
				0);

		for (int i = 2; i < points.length / 2; i += 2) {
			layer.attachChild(new Line(points[i - 2], points[i - 1], points[i],
					points[i + 1], vbom));
		}

		for (int i = 2 + points.length / 2; i < points.length; i += 2) {
			layer.attachChild(new Line(points[i - 2], points[i - 1], points[i],
					points[i + 1], vbom));
		}

		layer.attachChild(new Line(points[0], points[1],
				points[points.length / 2], points[points.length / 2 + 1], vbom));
		layer.attachChild(new Line(points[points.length / 2 - 2],
				points[points.length / 2 - 1], points[points.length - 2],
				points[points.length - 1], vbom));

		return layer;
	}

	public static Entity getDrawnMeshTriangles(List<Vector2> vertices,
			VertexBufferObjectManager vbom) {
		org.andengine.entity.Entity layer = new org.andengine.entity.Entity(0,
				0);
		for (int i = 2; i < vertices.size(); i += 3) {
			layer.attachChild(getDrawnTriangle(vertices.get(i - 2),
					vertices.get(i - 1), vertices.get(i), vbom));
		}
		return layer;
	}

	public static Entity getDrawnPolyline(List<Vector2> vertices,
			VertexBufferObjectManager vbom) {
		org.andengine.entity.Entity layer = new org.andengine.entity.Entity(0,
				0);
		for (int i = 1; i < vertices.size(); i++) {
			layer.attachChild(new Line(vertices.get(i - 1).x, vertices
					.get(i - 1).y, vertices.get(i).x, vertices.get(i).y, vbom));
		}
		layer.attachChild(new Line(vertices.get(0).x, vertices.get(0).y,
				vertices.get(vertices.size() - 1).x, vertices.get(vertices
						.size() - 1).y, vbom));
		return layer;
	}

	public static Entity getDrawnTriangle(Vector2 v1, Vector2 v2, Vector2 v3,
			VertexBufferObjectManager vbom) {
		org.andengine.entity.Entity layer = new org.andengine.entity.Entity(0,
				0);
		layer.attachChild(new Line(v1.x, v1.y, v2.x, v2.y, vbom));
		layer.attachChild(new Line(v2.x, v2.y, v3.x, v3.y, vbom));
		layer.attachChild(new Line(v3.x, v3.y, v1.x, v1.y, vbom));
		return layer;
	}

	public static Entity getDrawnBounds(Bounds bounds,
			VertexBufferObjectManager vbom) {
		org.andengine.entity.Entity layer = new org.andengine.entity.Entity(0,
				0);
		layer.attachChild(new Line(bounds.getLeft(), bounds.getTop(), bounds
				.getRight(), bounds.getTop(), vbom));
		layer.attachChild(new Line(bounds.getRight(), bounds.getTop(), bounds
				.getRight(), bounds.getBottom(), vbom));
		layer.attachChild(new Line(bounds.getRight(), bounds.getBottom(),
				bounds.getLeft(), bounds.getBottom(), vbom));
		layer.attachChild(new Line(bounds.getLeft(), bounds.getBottom(), bounds
				.getLeft(), bounds.getTop(), vbom));
		return layer;
	}
}
