package com.benjastudio.andengine.extension.comoncore.audio;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.andengine.audio.music.Music;
import org.andengine.engine.Engine;

import android.util.Log;

public class MusicManager {

	Engine engine;

	final public static String TAG = "MusicManager";

	Map<String, CustomMusic> musics;
	String currentMusic; // Current Music of the Scene
	Collection<String> playingMusics; // Current playing musics

	public MusicManager(Engine pEngine) {
		engine = pEngine;
		musics = new HashMap<String, CustomMusic>();
		playingMusics = Collections.synchronizedSet(new HashSet<String>());
	}

	public Map<String, CustomMusic> getMusics() {
		return musics;
	}

	public CustomMusic getMusic(String name) {
		if (name == null) {
			Log.e(TAG, "getMusic(null) ?");
			return null;
		}
		return musics.get(name);
	}

	public Music getRawMusic(String name) {
		CustomMusic customMusic = musics.get(name);
		if (customMusic == null) {
			Log.e(TAG, "getRawMusic(null) ??");
			return null;
		}
		return customMusic.music;
	}

	public void copyMusics(Map<String, CustomMusic> otherMusics) {
		musics.putAll(otherMusics);
	}

	public void addMusic(String pName, Music pMusic, float pMusicVolume,
			boolean pLooping) {
		// Log.d(TAG, "addMusic("+pName+", ...)");
		musics.put(pName, new CustomMusic(pMusic, pMusicVolume, engine,
				pLooping, pName));
	}

	public void removeMusic(String name) {
		// Log.d(TAG, "removeMusic("+name+")");
		musics.remove(name);
	}

	public void addPlayingMusic(String musicName) {
		// Log.d(TAG, "addPlayingMusic("+musicName+")");
		if (musicName != null/* && !musicName.equals(currentMusic) */)
			playingMusics.add(musicName);
	}

	public void removePlayingMusic(String musicName) {
		// Log.d(TAG, "removePlayingMusic("+musicName+")");
		if (playingMusics != null && musicName != null)
			playingMusics.remove(musicName);
	}

	public float getDuration(String musicName) {
		// Log.d(TAG, "getDuration("+musicName+")");
		if (musics.get(musicName) != null) {
			return musics.get(musicName).getDuration();
		} else {
			return 0;
		}
	}

	public void play(String musicName) {
		Log.d(TAG, "play(" + musicName + ")");
		if (musicName != null && musics.get(musicName) != null) {
			musics.get(musicName).play();
		}
		addPlayingMusic(musicName);
	}

	public void playFadeIn(String musicName, float t) {
		Log.d(TAG, "playFadeIn(" + musicName + "," + t + ")");
		if (musics.get(musicName) != null) {
			musics.get(musicName).playFadeIn(t);
		}
		addPlayingMusic(musicName);
	}

	public void playList(List<String> list) {
		Log.d(TAG, "playList(" + list + ")");
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				restoreVolume(list.get(i));
				play(list.get(i));
			}
		}
	}

	public void playListFadeIn(List<String> list, float t) {
		Log.d(TAG, "playListFadeIn(" + list + "," + t + ")");
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				playFadeIn(list.get(i), t);
			}
		}
	}

	public void stop(String musicName) {
		Log.d(TAG, "stop(" + musicName + ")");
		if (musicName != null && musics.get(musicName) != null) {
			musics.get(musicName).stop();
		}
		removePlayingMusic(musicName);
	}

	public void stopFadeOut(final String musicName, float t) {
		Log.d(TAG, "stopFadeOut(" + musicName + "," + t + ")");
		if (musics.get(musicName) != null) {
			musics.get(musicName).stopFadeOut(t, new Runnable() {
				@Override
				public void run() {
					removePlayingMusic(musicName);
				}
			});
		}
	}

	public void stopList(List<String> list) {
		Log.d(TAG, "stopList(" + list + ")");
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				stop(list.get(i));
			}
		}
	}

	public void stopListFadeOut(List<String> list, float t) {
		Log.d(TAG, "stopListFadeOut(" + list + "," + t + ")");
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				stopFadeOut(list.get(i), t);
			}
		}
	}

	public void pause(String musicName, boolean remove) {
		Log.d(TAG, "pause(" + musicName + ")");
		if (musicName != null && musics.get(musicName) != null) {
			musics.get(musicName).pause();
			if (remove)
				removePlayingMusic(musicName);
		}
	}

	public void pauseFadeOut(final String musicName, float t,
			final boolean remove) {
		Log.d(TAG, "pauseFadeOut(" + musicName + "," + t + ")");
		if (musics.get(musicName) != null) {
			musics.get(musicName).pauseFadeOut(t, new Runnable() {
				@Override
				public void run() {
					if (remove)
						removePlayingMusic(musicName);
				}
			});
		}
	}

	public void pauseList(List<String> list, boolean remove) {
		Log.d(TAG, "pauseList(" + list + ")");
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				pause(list.get(i), remove);
			}
		}
	}

	public void pauseListFadeOut(List<String> list, float t, boolean remove) {
		Log.d(TAG, "pauseListFadeOut(" + list + "," + t + ")");
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				pauseFadeOut(list.get(i), t, remove);
			}
		}
	}

	public void fade(String musicName, float v, float t) {
		Log.d(TAG, "fade(" + musicName + "," + v + "," + t + ")");
		if (musicName != null && musics.get(musicName) != null) {
			musics.get(musicName).fadeVolume(v, t);
		}
	}

	public void restoreVolumeWithFade(String musicName, float t) {
		Log.d(TAG, "restoreWithFade(" + musicName + "," + t + ")");
		if (musics.get(musicName) != null) {
			musics.get(musicName).restoreVolumeWithFade(t);
		}
	}

	public void restoreVolume(String musicName) {
		Log.d(TAG, "restoreVolume(" + musicName + ")");
		if (musics.get(musicName) != null) {
			musics.get(musicName).restoreVolume();
		}
	}

	private void finishActions() {
		Log.d(TAG, "finishActions()");
		String[] names = playingMusics
				.toArray(new String[playingMusics.size()]);
		for (String name : names) {
			Log.d(TAG, name + " -> finishAction()");
			if (musics.get(name) != null)
				musics.get(name).finishAction();
		}
	}

	public void mute() {
		Log.d(TAG, "mute()");
		finishActions();

		if (playingMusics != null) {
			synchronized (playingMusics) {
				for (String name : playingMusics)
					pause(name, false);
			}
		}
	}

	public void unmute() {
		Log.d(TAG, "unmute()");
		finishActions();

		if (playingMusics != null) {
			synchronized (playingMusics) {
				for (String name : playingMusics)
					play(name);
			}
		}
	}

	public static final String MUSIC_CONTINUE = "MUSIC_CONTINUE";

	public void onSceneChange(String fromMusic, String toMusic) {
		Log.d(TAG, "onSceneChange(" + fromMusic + ", " + toMusic + ")");

		if (toMusic == MUSIC_CONTINUE)
			return;
		else if (fromMusic != null && fromMusic.equals(toMusic)) {
			Log.d(TAG, "Same music, no changes.");
			return;
		}

		currentMusic = toMusic;

		if (fromMusic != null) {
			pauseFadeOut(fromMusic, 0.5f, true);
		}

		if (toMusic != null) {
			playFadeIn(toMusic, 0.5f);
		}
	}
}
