package com.benjastudio.andengine.extension.comoncore.resource;

import org.andengine.engine.Engine;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.ITextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.util.progress.IProgressListener;

import android.app.Activity;
import android.util.Log;

import com.benjastudio.andengine.extension.comoncore.ConstantVar;
import com.benjastudio.andengine.extension.comoncore.SettingsManager;
import com.benjastudio.andengine.extension.comoncore.audio.MusicManager;

public abstract class ResourceLoader {

	static public String TAG = "ResourceLoader";
	boolean loaded;
	public String name;
	protected MusicManager musicManager;
	protected Engine engine;
	protected Activity activity;
	protected int[] progressSteps;
	public TextureOptions textureQualityOptions;

	public ResourceLoader(Activity pActivity, Engine pEngine,
			MusicManager pMusicManager, String pName) {
		activity = pActivity;
		engine = pEngine;
		musicManager = pMusicManager;
		loaded = false;
		name = pName;
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("");

		setProgressSteps(new int[] { 0, 50, 60, 80, 90, 98, 100 });

		if (SettingsManager.getInstance().getQuality() >= ConstantVar.QUALITY_MEDIUM) {
			textureQualityOptions = TextureOptions.BILINEAR_PREMULTIPLYALPHA;
		} else {
			textureQualityOptions = TextureOptions.NEAREST_PREMULTIPLYALPHA;
		}
	}

	public boolean isLoaded() {
		return loaded;
	}

	public void setProgressSteps(int[] pProgressSteps) {
		progressSteps = pProgressSteps;
	}

	public void load(MultipleProgressListener pMultipleProgressListener) {

		if (SettingsManager.getInstance().getQuality() >= ConstantVar.QUALITY_MEDIUM) {
			textureQualityOptions = TextureOptions.BILINEAR_PREMULTIPLYALPHA;
		} else {
			textureQualityOptions = TextureOptions.NEAREST_PREMULTIPLYALPHA;
		}

		if (!loaded) {
			Log.d(TAG, name + " is loading...");

			pMultipleProgressListener.addDeeperLevel(progressSteps[0],
					progressSteps[1]);
			loadGraphics(pMultipleProgressListener);
			pMultipleProgressListener.backFromDeeperLevel();

			pMultipleProgressListener.addDeeperLevel(progressSteps[1],
					progressSteps[2]);
			loadFonts(pMultipleProgressListener);
			pMultipleProgressListener.backFromDeeperLevel();

			pMultipleProgressListener.addDeeperLevel(progressSteps[2],
					progressSteps[3]);
			loadInterface(pMultipleProgressListener);
			pMultipleProgressListener.backFromDeeperLevel();

			pMultipleProgressListener.addDeeperLevel(progressSteps[3],
					progressSteps[4]);
			loadMusics(pMultipleProgressListener);
			pMultipleProgressListener.backFromDeeperLevel();

			pMultipleProgressListener.addDeeperLevel(progressSteps[4],
					progressSteps[5]);
			loadSounds(pMultipleProgressListener);
			pMultipleProgressListener.backFromDeeperLevel();

			pMultipleProgressListener.addDeeperLevel(progressSteps[5],
					progressSteps[6]);
			loadUnclassified(pMultipleProgressListener);
			pMultipleProgressListener.backFromDeeperLevel();

			Log.d(TAG, name + " is loaded.");
			loaded = true;
		} else {
			Log.d(TAG, name + " already loaded.");
		}
	}

	public void load() {
		Log.v(TAG, name + " load");
		IProgressListener progressListener = new IProgressListener() {
			@Override
			public void onProgressChanged(int pProgress) {
			}
		};
		MultipleProgressListener multipleProgressListener = new MultipleProgressListener(
				progressListener);
		load(multipleProgressListener);
	}

	public void unload() {
		Log.d(TAG, name + " unload");
		if (loaded) {
			Log.d(TAG, name + " is unloading...");
			unloadGraphics();
			unloadFonts();
			unloadInterface();
			unloadMusics();
			unloadSounds();
			unloadUnclassified();
			Log.d(TAG, name + " is unloaded.");
			loaded = false;
		} else {
			Log.d(TAG, name + " already unloaded.");
		}
	}

	public void refresh() {
		Log.d(TAG, name + " refresh()");
		unload();
		load();
	}

	protected void unloadMusicFromName(String musicName) {
		Log.d(TAG, "unloadMusicFromName(" + musicName + ")");
		if (musicManager != null) {
			if (musicName != null) {
				musicManager.stop(musicName);
				musicManager.getRawMusic(musicName).release();
				engine.getMusicManager().remove(
						musicManager.getRawMusic(musicName));
				musicManager.removeMusic(musicName);
			}
		} else {
			Log.e(TAG, "unloadMusicFromName(" + musicName
					+ "): musicManager is null");
		}
	}

	protected void unloadAtlas(ITextureAtlas<?> atlas) {
		if (atlas != null) {
			if (atlas.isLoadedToHardware()) {
				atlas.unload();
			}
			atlas = null;
		}
	}

	protected abstract void loadGraphics(
			MultipleProgressListener pMultipleProgressListener);

	protected abstract void unloadGraphics();

	protected abstract void loadFonts(
			MultipleProgressListener pMultipleProgressListener);

	protected abstract void unloadFonts();

	protected abstract void loadInterface(
			MultipleProgressListener pMultipleProgressListener);

	protected abstract void unloadInterface();

	protected abstract void loadMusics(
			MultipleProgressListener pMultipleProgressListener);

	protected abstract void unloadMusics();

	protected abstract void loadSounds(
			MultipleProgressListener pMultipleProgressListener);

	protected abstract void unloadSounds();

	protected abstract void loadUnclassified(
			MultipleProgressListener pMultipleProgressListener);

	protected abstract void unloadUnclassified();
}
