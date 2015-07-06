package com.benjastudio.andengine.extension.comoncore.util;

import java.util.ArrayList;
import java.util.List;

import org.andengine.util.math.MathUtils;

import com.badlogic.gdx.math.Vector2;

public class GeomTool {

	public enum StripType {
		LEFT, RIGHT, TOP, BOTTOM, BOTTOMANDRIGHT, TOPANDLEFT, ;
	};

	public static boolean intersectLines(Vector2 l1p1, Vector2 l1p2,
			Vector2 l2p1, Vector2 l2p2, Vector2 intersection) {
		final float x1 = l1p1.x, y1 = l1p1.y, x2 = l1p2.x, y2 = l1p2.y, x3 = l2p1.x, y3 = l2p1.y, x4 = l2p2.x, y4 = l2p2.y;

		final float d = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
		if (d == 0)
			return false;

		if (intersection != null) {
			float ua = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)) / d;
			intersection.set(x1 + (x2 - x1) * ua, y1 + (y2 - y1) * ua);
		}
		return true;
	}

	public static Vector2 lineEquation(Vector2 p1, Vector2 p2) {
		final float a = (p2.y - p1.y) / (p2.x - p1.x);
		return new Vector2(a, p2.y - a * p2.x);
	}

	public static Vector2 normalVector(float a) {
		return new Vector2(1, -1 / a);
	}

	public static void rotate(float[] vertices, final float centerX,
			final float centerY, final float degrees) {
		if (degrees == 0)
			return;
		for (int i = 0; i < vertices.length / 2; i++) {
			float x = vertices[2 * i];
			float y = vertices[2 * i + 1];
			if (x != centerX || y != centerY) {
				x -= centerX;
				y -= centerY;
				float r = MathUtils.distance(0, 0, x, y);
				float teta = (float) Math.atan2(y, x);

				vertices[2 * i] = r
						* (float) Math.cos(teta + Math.toRadians(degrees))
						+ centerX;
				vertices[2 * i + 1] = r
						* (float) Math.sin(teta + Math.toRadians(degrees))
						+ centerY;
			}
		}
	}

	public static void scale(float[] vertices, final float scale) {
		if (scale == 1)
			return;
		for (int i = 0; i < vertices.length; i++) {
			vertices[i] *= scale;
		}
	}

	public static float[] vector2ListToFloatArray(
			final List<org.andengine.entity.primitive.Vector2> points) {
		final float points2[] = new float[points.size() * 2];
		int i = 0;
		for (org.andengine.entity.primitive.Vector2 vertex : points) {
			points2[i++] = vertex.x;
			points2[i++] = vertex.y;
		}
		return points2;
	}

	public static float[][] vector2ListToFloatXYArrays(
			final List<org.andengine.entity.primitive.Vector2> points) {
		final float x[] = new float[points.size()];
		final float y[] = new float[points.size()];
		int i = 0;
		for (org.andengine.entity.primitive.Vector2 vertex : points) {
			x[i] = vertex.x;
			y[i] = vertex.y;
			i++;
		}
		return new float[][] { x, y };
	}

	public static float[] floatXYArraysToFloatArray(final float x[], float y[]) {
		final float points[] = new float[x.length * 2];
		int i = 0;
		for (int j = 0; j < x.length; j++) {
			points[i++] = x[j];
			points[i++] = y[j];
		}
		return points;
	}

	public static List<org.andengine.entity.primitive.Vector2> floatXYArraysToVector2List(
			final float x[], float y[]) {
		final List<org.andengine.entity.primitive.Vector2> points = new ArrayList<org.andengine.entity.primitive.Vector2>();

		for (int i = 0; i < x.length; i++) {
			points.add(new org.andengine.entity.primitive.Vector2(x[i], y[i]));
		}
		return points;
	}

	public static List<org.andengine.entity.primitive.Vector2> floatArrayToVector2List(
			final float points[]) {

		final List<org.andengine.entity.primitive.Vector2> points2 = new ArrayList<org.andengine.entity.primitive.Vector2>();

		for (int i = 0; i < points.length; i += 2) {
			points2.add(new org.andengine.entity.primitive.Vector2(points[i],
					points[i + 1]));
		}
		return points2;
	}

	public static float[][] floatArrayToFloatXYArrays(final float points[]) {
		final float x[] = new float[points.length / 2];
		final float y[] = new float[points.length / 2];
		int j = 0;
		for (int i = 0; i < x.length; i++) {
			x[i] = points[j++];
			y[i] = points[j++];
		}
		return new float[][] { x, y };
	}

	public static float middleOf(float a, float b) {
		return 0.5f * (a + b);
	}

	public static void subdivideAndSmoothPolygon(
			List<org.andengine.entity.primitive.Vector2> points, int n) {
		for (int i = 0; i < n; i++) {
			subdividePolygon(points);
			smoothPolygon(points);
		}
	}

	public static float getAngleRad(float xa, float ya, float xb, float yb) {
		return (float) Math.atan2(yb - ya, xb - xa);
	}

	public static float getAngleDegrees(float xa, float ya, float xb, float yb) {
		return (float) Math.toDegrees(getAngleRad(xa, ya, xb, yb));
	}

	public static org.andengine.entity.primitive.Vector2 getMiddleVertex(
			float xa, float ya, float xb, float yb) {
		return new org.andengine.entity.primitive.Vector2(0.5f * (xa + xb),
				0.5f * (ya + yb));
	}

	public static void subdividePolygon(
			List<org.andengine.entity.primitive.Vector2> points) {
		// Subdivide
		for (int i = 1; i < points.size(); i += 2) {
			points.add(
					i,
					getMiddleVertex(points.get(i - 1).x, points.get(i - 1).y,
							points.get(i).x, points.get(i).y));
		}
		points.add(getMiddleVertex(points.get(0).x, points.get(0).y,
				points.get(points.size() - 1).x,
				points.get(points.size() - 1).y));
	}

	public static void smoothPolygon(
			List<org.andengine.entity.primitive.Vector2> points) {
		points.get(0).x = 0.5f * (points.get(0).x + 0.5f * (points.get(points
				.size() - 1).x + points.get(1).x));
		points.get(0).y = 0.5f * (points.get(0).y + 0.5f * (points.get(points
				.size() - 1).y + points.get(1).y));
		for (int i = 2; i < points.size(); i += 2) {
			points.get(i).x = 0.5f * (points.get(i).x + 0.5f * (points
					.get(i - 1).x + points.get(i + 1).x));
			points.get(i).y = 0.5f * (points.get(i).y + 0.5f * (points
					.get(i - 1).y + points.get(i + 1).y));
		}
	}

	/**
	 * @param points
	 *            must be order in clockwise
	 */
	private static boolean isTopSegment(
			org.andengine.entity.primitive.Vector2 p1,
			org.andengine.entity.primitive.Vector2 p2) {
		return p2.x - p1.x > 0;
	}

	/**
	 * @param points
	 *            must be order in clockwise
	 */
	private static boolean isBottomSegment(
			org.andengine.entity.primitive.Vector2 p1,
			org.andengine.entity.primitive.Vector2 p2) {
		return !isTopSegment(p1, p2);
	}

	/**
	 * @param points
	 *            must be order in clockwise
	 */
	private static boolean isRightSegment(
			org.andengine.entity.primitive.Vector2 p1,
			org.andengine.entity.primitive.Vector2 p2) {
		return p2.y - p1.y > 0;
	}

	/**
	 * @param points
	 *            must be order in clockwise
	 */
	private static boolean isLeftSegment(
			org.andengine.entity.primitive.Vector2 p1,
			org.andengine.entity.primitive.Vector2 p2) {
		return !isRightSegment(p1, p2);
	}

	/**
	 * @param points
	 *            must be order in clockwise
	 */
	private static boolean isSegmentTypeOf(StripType type,
			org.andengine.entity.primitive.Vector2 p1,
			org.andengine.entity.primitive.Vector2 p2) {
		if (type == StripType.TOP)
			return isTopSegment(p1, p2);
		if (type == StripType.BOTTOMANDRIGHT)
			return isBottomSegment(p1, p2) && isRightSegment(p1, p2);
		if (type == StripType.TOPANDLEFT)
			return isTopSegment(p1, p2) && isLeftSegment(p1, p2);
		if (type == StripType.LEFT)
			return isLeftSegment(p1, p2);
		if (type == StripType.RIGHT)
			return isRightSegment(p1, p2);
		if (type == StripType.BOTTOM)
			return isBottomSegment(p1, p2);
		else
			return false;
	}

	/**
	 * @param points
	 *            must be order in clockwise
	 */
	private static org.andengine.entity.primitive.Vector2 getNormalVertex(
			org.andengine.entity.primitive.Vector2 p1,
			org.andengine.entity.primitive.Vector2 p2) {
		return new org.andengine.entity.primitive.Vector2(-p2.y + p1.y, p2.x
				- p1.x);
	}

	/**
	 * @param p0
	 *            vertex before
	 * @param p1
	 *            vertex
	 * @param p2
	 *            vertex after points must be order in clockwise
	 * */
	private static org.andengine.entity.primitive.Vector2 getExtrudedVertex(
			float normalizeInfluence, float coefficientX, float coefficientY,
			org.andengine.entity.primitive.Vector2 p0,
			org.andengine.entity.primitive.Vector2 p1,
			org.andengine.entity.primitive.Vector2 p2) {
		org.andengine.entity.primitive.Vector2 normalBefore = getNormalVertex(
				p0, p1);
		org.andengine.entity.primitive.Vector2 normalAfter = getNormalVertex(
				p1, p2);

		normalizeWithInfluence(normalBefore, normalizeInfluence);
		normalizeWithInfluence(normalAfter, normalizeInfluence);

		org.andengine.entity.primitive.Vector2 extruded = new org.andengine.entity.primitive.Vector2(
				p1.x
						+ (coefficientX * 8 * 0.5f * (normalBefore.x + normalAfter.x)),
				p1.y
						+ (coefficientY * 8 * 0.5f * (normalBefore.y + normalAfter.y)));
		return extruded;
	}

	/**
	 * @param p0
	 *            vertex before
	 * @param p1
	 *            vertex after
	 * @param distance
	 *            distance from the center of the p0-p1 edge points must be
	 *            order in clockwise
	 * */
	public static org.andengine.entity.primitive.Vector2 getExtrudedVertexAtMiddle(
			org.andengine.entity.primitive.Vector2 p0,
			org.andengine.entity.primitive.Vector2 p1, float distance) {
		final org.andengine.entity.primitive.Vector2 center = getMiddleVertex(
				p0.x, p0.y, p1.x, p1.y);
		final org.andengine.entity.primitive.Vector2 normal = getNormalVertex(
				p0, p1);
		normalize(normal);
		return new org.andengine.entity.primitive.Vector2(center.x + normal.x
				* distance, center.y + normal.y * distance);
	}

	private static void normalize(org.andengine.entity.primitive.Vector2 vector) {
		float length = MathUtils.distance(0, 0, vector.x, vector.y);
		if (length != 0) {
			vector.x = vector.x / length;
			vector.y = vector.y / length;
		}
	}

	/**
	 * @param influence
	 *            Must be between 0f and 1f. The influence of the normalization,
	 *            1f for full normalization, 0f for no normalization.
	 * */
	private static void normalizeWithInfluence(
			org.andengine.entity.primitive.Vector2 vector, float influence) {
		org.andengine.entity.primitive.Vector2 normalized = new org.andengine.entity.primitive.Vector2(
				vector.x, vector.y);
		normalize(normalized);
		vector.x = 0.5f * (vector.x * (1 - influence) + normalized.x
				* (influence));
		vector.y = 0.5f * (vector.y * (1 - influence) + normalized.y
				* (influence));
	}

	public static void multiply(org.andengine.entity.primitive.Vector2 vector,
			float a) {
		vector.x *= a;
		vector.y *= a;
	}

	/**
	 * @param points
	 *            in clock wised order
	 * @param getTops
	 *            if true return tops else return bottoms
	 * */
	public static List<ArrayList<org.andengine.entity.primitive.Vector2>> getStrips(
			StripType stripType,
			List<org.andengine.entity.primitive.Vector2> points) {
		final List<ArrayList<org.andengine.entity.primitive.Vector2>> stripCollection = new ArrayList<ArrayList<org.andengine.entity.primitive.Vector2>>();

		ArrayList<org.andengine.entity.primitive.Vector2> strip = new ArrayList<org.andengine.entity.primitive.Vector2>();

		boolean isBeginStrip = false;

		if (isSegmentTypeOf(stripType, points.get(points.size() - 1),
				points.get(0))) {
			strip.add(points.get(points.size() - 1));
			strip.add(points.get(0));
			isBeginStrip = true;
		}

		for (int i = 1; i < points.size(); i++) {
			if (isSegmentTypeOf(stripType, points.get(i - 1), points.get(i))) {
				if (strip.size() == 0) {
					strip.add(points.get(i - 1));
				}
				strip.add(points.get(i));
			} else {
				if (strip.size() > 0) {
					stripCollection.add(strip);
					strip = new ArrayList<org.andengine.entity.primitive.Vector2>();
				}
			}
		}

		// attach last strip
		if (strip.size() > 0) {
			// connect it to first strip segment
			if (isBeginStrip) {
				if (strip.get(strip.size() - 1).x == points
						.get(points.size() - 1).x
						&& strip.get(strip.size() - 1).y == points.get(points
								.size() - 1).y) {
					strip.addAll(stripCollection.get(0));
					stripCollection.set(0, strip);

				} else {
					stripCollection.add(strip);
				}
			} else {
				stripCollection.add(strip);
			}
		}

		return stripCollection;
	}

	public static float[] getRowMajoredMeshFromPolyline(
			ArrayList<org.andengine.entity.primitive.Vector2> polyline) {
		return getRowMajoredMeshFromPolyline(polyline, 0, 0, 1.3f, 1.3f);
	}

	public static float[] getRowMajoredMeshFromPolyline(
			List<org.andengine.entity.primitive.Vector2> polyline,
			float resizeCoefficientXRow1, float resizeCoefficientYRow1,
			float resizeCoefficientXRow2, float resizeCoefficientYRow2) {

		float[] points = new float[polyline.size() * 2 * 2];

		// 1st row
		int i = 0;
		for (int j = 0; j < polyline.size(); j++) {
			int jm1 = j - 1, jp1 = j + 1;
			if (jm1 < 0)
				jm1 = 0;
			if (jp1 >= polyline.size())
				jp1 = polyline.size() - 1;

			org.andengine.entity.primitive.Vector2 extruded = getExtrudedVertex(
					0.8f, -resizeCoefficientXRow1, -resizeCoefficientYRow1,
					polyline.get(jm1), polyline.get(j), polyline.get(jp1));
			points[i++] = extruded.x;
			points[i++] = extruded.y;
		}

		// 2nd row
		for (int j = 0; j < polyline.size(); j++) {
			int jm1 = j - 1, jp1 = j + 1;
			if (jm1 < 0)
				jm1 = 0;
			if (jp1 >= polyline.size())
				jp1 = polyline.size() - 1;

			org.andengine.entity.primitive.Vector2 extruded = getExtrudedVertex(
					0.8f, resizeCoefficientXRow2, resizeCoefficientYRow2,
					polyline.get(jm1), polyline.get(j), polyline.get(jp1));
			points[i++] = extruded.x;
			points[i++] = extruded.y;
		}

		return points;
	}

	public static Bounds getBounds(final float[] verts) {
		float minX, maxX, minY, maxY;
		minX = maxX = verts[0];
		minY = maxY = verts[1];
		for (int i = 0; i < verts.length / 2; i++) {
			final float x = verts[2 * i];
			final float y = verts[2 * i + 1];
			if (x < minX)
				minX = x;
			else if (x > maxX)
				maxX = x;
			if (y < minY)
				minY = y;
			else if (y > maxY)
				maxY = y;
		}
		return new Bounds(minX, minY, maxX, maxY);
	}

	public static Bounds getBounds(
			List<org.andengine.entity.primitive.Vector2> points) {
		float minX, maxX, minY, maxY;
		minX = maxX = points.get(0).x;
		minY = maxY = points.get(0).y;
		for (org.andengine.entity.primitive.Vector2 point : points) {
			if (point.x < minX)
				minX = point.x;
			else if (point.x > maxX)
				maxX = point.x;
			if (point.y < minY)
				minY = point.y;
			else if (point.y > maxY)
				maxY = point.y;
		}
		return new Bounds(minX, minY, maxX, maxY);
	}

	public static Bounds trimOffset(
			List<org.andengine.entity.primitive.Vector2> points, int margin) {
		Bounds bounds = getBounds(points);
		bounds.resizeOf(-margin, -margin, margin, margin);
		offset(points, -bounds.getOffsetX(), -bounds.getOffsetY());
		return bounds;
	}

	public static void offset(
			List<org.andengine.entity.primitive.Vector2> points, float dx,
			float dy) {
		for (org.andengine.entity.primitive.Vector2 point : points) {
			point.x += dx;
			point.y += dy;
		}
	}

	public static void offset(float[] points, float dx, float dy) {
		for (int i = 0; i < points.length; i += 2) {
			points[i] += dx;
			points[i + 1] += dy;
		}
	}

	public static float getLength(float[] points) {
		float length = 0;
		for (int i = 2; i < points.length; i += 2) {
			length += MathUtils.distance(points[i - 2], points[i - 1],
					points[i], points[i + 1]);
		}
		return length;
	}
}
