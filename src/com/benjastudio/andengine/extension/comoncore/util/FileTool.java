package com.benjastudio.andengine.extension.comoncore.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.util.Log;

public class FileTool {

	private final static String TAG = "FileTool";

	public static boolean assetExists(Activity activity, String path) {
		try {
			activity.getAssets().open(path).close();
			return true;
		} catch (IOException e) {
			return false;
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			return false;
		}
	}

	public static boolean fileExists(String path) {
		return path != null && new File(path).exists();
	}

	public static int[] getAssetImageSize(Activity activity, String path) {
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			InputStream inputStream;
			inputStream = activity.getAssets().open(path);
			BitmapFactory.decodeStream(inputStream, null, options);
			inputStream.close();
			return new int[] { options.outWidth, options.outHeight };

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String readTxtFile(Activity activity, String filename) {

		AssetManager am = activity.getAssets();

		try {

			InputStream inputStream = am.open(filename);
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

			int i = inputStream.read();
			while (i != -1) {
				byteArrayOutputStream.write(i);
				i = inputStream.read();
			}
			inputStream.close();

			return byteArrayOutputStream.toString();

		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static void ls(String absolutePath, boolean recursive, String tag) {
		if (absolutePath == null)
			return;
		final File file = new File(absolutePath);
		if (file.isDirectory()) {
			for (final File fileEntry : file.listFiles()) {
				if (fileEntry != null) {
					if (fileEntry.isDirectory())
						Log.d(tag, "directory: " + fileEntry.getAbsolutePath());
					else
						Log.d(tag, "file: " + fileEntry.getAbsolutePath());
					if (fileEntry.isDirectory() && recursive)
						ls(fileEntry.getAbsolutePath(), recursive, tag);
				}
			}
		} else if (file.isFile()) {
			Log.d(tag, "file: " + file.getAbsolutePath());
		} else if (!file.exists()) {
			Log.d(tag, absolutePath + " doesn't exist.");
		}
	}
}
