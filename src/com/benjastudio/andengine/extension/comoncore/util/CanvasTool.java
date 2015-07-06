package com.benjastudio.andengine.extension.comoncore.util;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;

public class CanvasTool {

	public static void drawRepeatedBackground(Canvas pCanvas, Bounds bounds,
			Bitmap texture, TileMode tileModeX, TileMode tileModeY) {
		BitmapDrawable drawable = new BitmapDrawable(texture);
		drawable.setBounds(bounds.getRelativeRect());
		drawable.setTileModeXY(tileModeX, tileModeY);
		drawable.draw(pCanvas);
	}

	public static void drawColoredBackground(Canvas pCanvas, Bounds bounds,
			int color) {
		Paint paint = createPaint();
		paint.setColor(color);
		pCanvas.drawRect(bounds.getRelativeRect(), paint);
	}

	public static void drawBitmapMesh(Canvas pCanvas, Bounds textureBounds,
			float[] rowMajoredMesh, Bitmap texture) {
		Paint paint = createPaint();
		Bitmap bitmap = Bitmap.createBitmap(textureBounds.getWidthInt(),
				textureBounds.getHeightInt(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawRepeatedBackground(canvas, textureBounds, texture, TileMode.REPEAT,
				TileMode.CLAMP);
		pCanvas.drawBitmapMesh(bitmap, rowMajoredMesh.length / 4 - 1, 1,
				rowMajoredMesh, 0, null, 0, paint);
		bitmap.recycle();
	}

	public static void cropVertices(Canvas pCanvas, Bounds bounds,
			float[] vertices) {
		Bitmap bitmap = Bitmap.createBitmap(bounds.getWidthInt(),
				bounds.getHeightInt(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		Paint paint = createPaint();
		paint.setColor(android.graphics.Color.WHITE);
		paint.setStyle(Paint.Style.FILL);
		int[] colors = new int[vertices.length];
		for (int i = 0; i < colors.length; i++) {
			colors[i] = android.graphics.Color.WHITE;
		}
		canvas.drawVertices(Canvas.VertexMode.TRIANGLES, vertices.length,
				vertices, 0, vertices, 0, colors, 0, null, 0, 0, paint);

		// Multiply white mesh pixels by texture
		paint.setXfermode(new PorterDuffXfermode(Mode.MULTIPLY));
		final BitmapShader bn = new BitmapShader(bitmap, TileMode.REPEAT,
				TileMode.REPEAT);
		paint.setShader(bn);
		pCanvas.drawRect(bounds.getRelativeRect(), paint);
		paint.setXfermode(null);
		bitmap.recycle();
	}

	public static void drawColoredRowMajoredMesh(Canvas pCanvas, Bounds bounds,
			float[] rowMajoredMesh, int color) {
		Bitmap bitmap = Bitmap.createBitmap(bounds.getWidthInt(),
				bounds.getHeightInt(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawColoredBackground(canvas, bounds, color);
		Paint paint = createPaint();
		pCanvas.drawBitmapMesh(bitmap, rowMajoredMesh.length / 4 - 1, 1,
				rowMajoredMesh, 0, null, 0, paint);
		bitmap.recycle();
	}

	public static void paste(Canvas pCanvas, Bitmap bitmap, float left,
			float top, Xfermode xfermode, int color) {
		Paint paint = createPaint();
		paint.setXfermode(xfermode);
		paint.setColor(color);
		pCanvas.drawBitmap(bitmap, left, top, paint);
		paint.setXfermode(null);
	}

	public static void pasteWithRotation(Canvas pCanvas, Bitmap bitmap,
			float left, float top, float degrees, Xfermode xfermode, int color) {
		Paint paint = createPaint();
		if (xfermode != null)
			paint.setXfermode(xfermode);
		paint.setColor(color);
		pCanvas.save();
		pCanvas.rotate(degrees, left + bitmap.getWidth() * 0.5f,
				top + bitmap.getHeight() * 0.5f);
		pCanvas.drawBitmap(bitmap, left, top, paint);
		pCanvas.restore();
		paint.setXfermode(null);
	}

	public static void applyColorEffect(Canvas pCanvas,
			float[] pRowMajoredMesh, int colorMesh, Xfermode xfermode,
			int paintColor) {
		Bounds bounds = GeomTool.getBounds(pRowMajoredMesh);
		Bitmap bitmap = Bitmap.createBitmap(bounds.getWidthInt(),
				bounds.getHeightInt(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		// Offset mesh according to bitmap coordinates
		float[] rowMajoredMesh = pRowMajoredMesh.clone();
		GeomTool.offset(rowMajoredMesh, -bounds.getOffsetX(),
				-bounds.getOffsetY());

		drawColoredRowMajoredMesh(canvas, bounds, rowMajoredMesh, colorMesh);

		paste(pCanvas, bitmap, bounds.getOffsetX(), bounds.getOffsetY(),
				xfermode, paintColor);
		bitmap.recycle();
	}

	public static void darken(Canvas pCanvas, float[] pRowMajoredMesh,
			float intensity) {
		applyColorEffect(pCanvas, pRowMajoredMesh, Color.BLACK,
				new PorterDuffXfermode(Mode.SRC_ATOP),
				android.graphics.Color.argb((int) (255 * intensity), 0, 0, 0));
	}

	public static void lighten(Canvas pCanvas, float[] pRowMajoredMesh,
			float intensity) {
		applyColorEffect(pCanvas, pRowMajoredMesh, Color.YELLOW,
				new PorterDuffXfermode(Mode.SCREEN),
				android.graphics.Color.argb((int) (255 * intensity), 0, 0, 0));
	}

	public static void applyFog(Canvas pCanvas, Bounds bounds, float coefficient) {
		if (coefficient == 0)
			return;
		Paint paint = createPaint();
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_ATOP));
		paint.setColor(android.graphics.Color.argb((int) (255f * coefficient),
				255, 255, 255));
		pCanvas.drawRect(bounds.getRelativeRect(), paint);
		paint.setXfermode(null);
	}

	public static void applyGradient(Canvas pCanvas, Bounds bounds,
			int fromColor, int toColor, float x0, float y0, float x1, float y1) {
		Paint paint = createPaint();
		paint.setXfermode(new PorterDuffXfermode(Mode.MULTIPLY));
		LinearGradient f = new LinearGradient(x0 * bounds.getWidth(), y0
				* bounds.getHeight(), x1 * bounds.getWidth(), y1
				* bounds.getHeight(), fromColor, toColor, TileMode.CLAMP);
		paint.setShader(f);
		pCanvas.drawRect(bounds.getRelativeRect(), paint);
		paint.setXfermode(null);
	}

	public static void drawOutline(Canvas pCanvas, float[] vertices,
			int fromId, int toId, int color, float thickness) {
		Paint paint = createPaint();
		paint.setColor(color);
		paint.setStrokeWidth(thickness);

		for (int i = fromId; i < vertices.length && i < toId; i += 2) {
			int xa = i, ya = xa + 1;
			int xb = (i + 2 < vertices.length) ? i + 2 : 0;
			int yb = xb + 1;

			pCanvas.drawLine(vertices[xa], vertices[ya], vertices[xb],
					vertices[yb], paint);
		}
	}

	public static Paint createPaint() {
		Paint paint = new Paint();
		initPaint(paint);
		return paint;
	}

	public static void initPaint(Paint pPaint) {
		pPaint.setAntiAlias(true);
		pPaint.setDither(true);
		pPaint.setShader(null);
	}
}
