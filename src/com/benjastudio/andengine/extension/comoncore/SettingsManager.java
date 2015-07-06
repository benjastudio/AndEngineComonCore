package com.benjastudio.andengine.extension.comoncore;

import com.benjastudio.andengine.extension.comoncore.audio.MusicManager;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SettingsManager {

	// private final static String TAG = "SettingsManager";

	private SharedPreferences gamePreferences;
	private SharedPreferences.Editor gamePreferencesEditor;
	private SharedPreferences viewedStories;
	private SharedPreferences.Editor viewedStoriesEditor;

	Activity activity;
	MusicManager musicManager;

	private boolean mute, vibrator, debug;
	private int quality;

	private static volatile SettingsManager instance;

	private SettingsManager() {
		super();
	}

	public static SettingsManager getInstance() {
		if (instance == null) {
			synchronized (SettingsManager.class) {
				if (instance == null) {
					instance = new SettingsManager();
				}
			}
		}
		return instance;
	}

	public void init(Activity pActivity, MusicManager pMusicManager) {
		activity = pActivity;
		musicManager = pMusicManager;
		gamePreferences = activity.getSharedPreferences(
				ConstantVar.GAMEPREFERENCES_DB_NAME, Context.MODE_PRIVATE);
		gamePreferencesEditor = gamePreferences.edit();
		viewedStories = activity.getSharedPreferences(
				ConstantVar.VIEWEDSTORIES_DB_NAME, Context.MODE_PRIVATE);
		viewedStoriesEditor = viewedStories.edit();
		mute = getMuteFromDb();
		quality = getQualityFromDb();
		vibrator = getVibratorFromDb();
		applyMute();
	}

	public int getQuality() {
		return quality;
	}

	public int getQualityFromDb() {
		return gamePreferences.getInt("quality", ConstantVar.QUALITY_DEFAULT);
	}

	public void setQuality(int pQuality) {
		if (pQuality == getQuality())
			return;
		gamePreferencesEditor.putInt("quality", pQuality);
		gamePreferencesEditor.commit();
		quality = pQuality;
	}

	public boolean getMuteFromDb() {
		return gamePreferences.getBoolean("mute", false);
	}

	public boolean getVibrator() {
		return vibrator;
	}

	public boolean getVibratorFromDb() {
		return gamePreferences.getBoolean("vibrator", true);
	}

	public boolean getMute() {
		return mute;
	}

	public boolean getDebug() {
		return debug;
	}

	public void applyMute() {

		if (getMute()) {
			musicManager.mute();
		} else {
			musicManager.unmute();
		}
	}

	public void setMute(boolean state) {

		if (state == getMute())
			return;
		gamePreferencesEditor.putBoolean("mute", state);
		gamePreferencesEditor.commit();

		mute = state;

		applyMute();
	}

	public void setVibrator(boolean state) {

		if (state == getVibrator())
			return;
		gamePreferencesEditor.putBoolean("vibrator", state);
		gamePreferencesEditor.commit();

		vibrator = state;
	}

	public void setDebug(boolean state) {
		if (state == getDebug())
			return;
		gamePreferencesEditor.putBoolean("debug", state);
		gamePreferencesEditor.commit();

		debug = state;
	}

	public void setCurrentWorld(int pWorld) {
		gamePreferencesEditor.putInt("currentWorld", pWorld);
		gamePreferencesEditor.commit();
	}

	public int getCurrentWorld() {
		return gamePreferences.getInt("currentWorld", 1);
	}

	public void setViewedStory(int pWorld, boolean viewed) {
		viewedStoriesEditor.putBoolean(String.valueOf(pWorld), viewed);
		viewedStoriesEditor.commit();
	}

	public void setStoryViewedForCurrentSelectedWorld(boolean viewed) {
		setViewedStory(getCurrentWorld(), viewed);
	}

	public boolean isStoryViewed(int pWorld) {
		return viewedStories.getBoolean(String.valueOf(pWorld), false);
	}

	public boolean isStoryViewedForCurrentSelectedWorld() {
		return viewedStories.getBoolean(String.valueOf(getCurrentWorld()),
				false);
	}

	public void clearViewedStories() {
		viewedStoriesEditor.clear();
		viewedStoriesEditor.commit();
	}
}