package com.benjastudio.andengine.extension.comoncore.util;

import org.andengine.entity.primitive.Vector2;

import android.graphics.Rect;

public class Bounds {

	private Rect rect;
	private float getCoefficient = 1f;

	public Bounds(Rect pRect) {
		set(pRect);
	}

	public Bounds(float left, float top, float right, float bottom) {
		set(left, top, right, bottom);
	}

	public void set(Rect pRect) {
		if (rect == null)
			rect = new Rect(pRect);
		else
			rect.set(pRect);
	}

	public void set(float left, float top, float right, float bottom) {
		if (rect == null)
			rect = new Rect((int) left, (int) top, (int) right, (int) bottom);
		else
			rect.set((int) left, (int) top, (int) right, (int) bottom);
	}

	public void resizeOf(float dxFromleft, float dyFromtop, float dxFromright,
			float dyFrombottom) {
		set(getLeft() + dxFromleft, getTop() + dyFromtop, getRight()
				+ dxFromright, getBottom() + dyFrombottom);
	}

	public void offsetTo(float newLeft, float newTop) {
		rect.offsetTo((int) newLeft, (int) newTop);
	}

	public void offset(float dx, float dy) {
		rect.offset((int) dx, (int) dy);
	}

	/**
	 * All next returned values from getSomething() will be multiplied by
	 * coefficient. setSomething() and offsetSomething() methods will not be
	 * affected.
	 */
	public void setGetCoefficient(float pGetCoefficient) {
		getCoefficient = pGetCoefficient;
	}

	/**
	 * Set coefficient to 1f.
	 */
	public void resetGetCoefficient() {
		getCoefficient = 1f;
	}

	private float pass(float a) {
		if (getCoefficient == 1f)
			return a;
		return getCoefficient * a;
	}

	public final Rect getRect() {
		return new Rect((int) getLeft(), (int) getTop(), (int) getRight(),
				(int) getBottom());
	}

	public Rect getRelativeRect() {
		return new Rect(0, 0, (int) getWidth(), (int) getHeight());
	}

	public float getWidth() {
		return pass(rect.right - rect.left);
	}

	public int getWidthInt() {
		return (int) getWidth();
	}

	public float getHeight() {
		return pass(rect.bottom - rect.top);
	}

	public int getHeightInt() {
		return (int) getHeight();
	}

	public float getLeft() {
		return pass(rect.left);
	}

	public float getOffsetX() {
		return getLeft();
	}

	public float getRight() {
		return pass(rect.right);
	}

	public float getTop() {
		return pass(rect.top);
	}

	public float getOffsetY() {
		return getTop();
	}

	public float getBottom() {
		return pass(rect.bottom);
	}

	public Vector2 getOffset() {
		return new Vector2(getLeft(), getTop());
	}

	public float getCenterPositionX() {
		return getWidth() * 0.5f + getLeft();
	}

	public float getCenterPositionY() {
		return getHeight() * 0.5f + getTop();
	}

	public Vector2 getCenterPosition() {
		return new Vector2(getCenterPositionX(), getCenterPositionY());
	}

	public float getMiddleX() {
		return getWidth() * 0.5f;
	}

	public float getMiddleY() {
		return getHeight() * 0.5f;
	}

	public Vector2 getMiddle() {
		return new Vector2(getMiddleX(), getMiddleY());
	}
}
