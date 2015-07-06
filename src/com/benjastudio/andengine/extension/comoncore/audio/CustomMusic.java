package com.benjastudio.andengine.extension.comoncore.audio;

import java.util.UUID;

import org.andengine.audio.music.Music;
import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;

import android.provider.SyncStateContract.Constants;

import com.benjastudio.andengine.extension.comoncore.ConstantVar;
import com.benjastudio.andengine.extension.comoncore.SettingsManager;

public class CustomMusic {

	public Music music;
	String name;
	float default_volume;
	Engine engine;
	int duration;
	UUID currentActionId;
	Runnable finalAction;
	Runnable callback;

	static final public String TAG = "CustomMusic";
	static final public Runnable CALLBACK_EMPTY = new Runnable() {
		@Override
		public void run() {
		}
	};

	public CustomMusic(Music pMusic, float pDefault_volume, Engine pEngine,
			boolean pLooping, String pName) {
		music = pMusic;
		default_volume = pDefault_volume;
		engine = pEngine;
		duration = pMusic.getMediaPlayer().getDuration();
		name = pName;
		if (music != null) {
			music.setLooping(pLooping);
		}
		generateNewActionId();
		finalAction = null;
	}

	private void log(String text) {
		// Log.d(TAG, text+
		// " ("+name+", "+currentActionId.toString().substring(0,4)+")");
	}

	/** In order to stop the current action if a new one comes. */
	private UUID generateNewActionId() {
		final UUID actionId = UUID.randomUUID();
		currentActionId = actionId;
		log("generateNewActionId()");
		return actionId;
	}

	/** Check the validity of the action passed in parameter */
	private boolean isActionValid(UUID pActionId) {
		return currentActionId.compareTo(pActionId) == 0;
	}

	private void generateDirectAction() {
		log("generateDirectAction()");
		finalAction = null;
		callback = null;
	}

	private void generateDelayedAction(Runnable pFinalAction, Runnable pCallback) {
		log("generateDelayedAction(" + pFinalAction + ", " + pCallback + ")");
		finalAction = pFinalAction;
		callback = pCallback;
	}

	public void finishAction() {
		if (finalAction != null) {
			log("finishAction");
			finalAction.run();
			finalAction = null;
		} else {
			log("finishAction (null)");
		}
	}

	private void onActionFinished() {
		if (callback != null) {
			log("run callback");
			callback.run();
			callback = null;
			finalAction = null;
		}
	}

	public float getDuration() {
		return ((float) duration) / 1000f;
	}

	public void setVolume(float v) {
		final UUID actionId = generateNewActionId();
		generateDirectAction();
		setVolume(v, actionId);
	}

	public void setVolume(float v, final UUID pActionId) {
		log("setVolume(" + v + ")");
		if (isActionValid(pActionId)) {
			music.setVolume(v);
		}
	}

	public void fadeVolume(final float v, float t) {
		fadeVolume(v, t, CALLBACK_EMPTY);
	}

	public void fadeVolume(final float v, float t, Runnable pCallback) {
		final UUID actionId = generateNewActionId();
		generateDelayedAction(new Runnable() {
			@Override
			public void run() {
				setVolume(v, actionId);
			}
		}, pCallback);
		fadeVolume(v, t, actionId, null);
	}

	private void fadeVolume(final float v, float t, final UUID pActionId,
			final CustomMusicCallback callback) {
		log("fadeVolume(" + v + "," + t + ")");
		if (isActionValid(pActionId)) {
			if (music != null) {
				float delta_t = 0.05f;
				final float vFrom = music.getVolume();
				final float volumeIncrement = (v - vFrom) / (t / delta_t);

				log("fadeVolume: volumeIncrement=" + volumeIncrement
						+ ", vFrom=" + vFrom + ", delta_t=" + delta_t);

				engine.registerUpdateHandler(new TimerHandler(delta_t, false,
						new ITimerCallback() {
							public void onTimePassed(
									final TimerHandler pTimerHandler) {
								if (isActionValid(pActionId)) {

									if (music.getVolume() == v) {
										if (callback != null)
											callback.onCompleted(pActionId);
									} else {
										final float nextVolume = music
												.getVolume() + volumeIncrement;
										if ((v > vFrom && nextVolume < v)
												|| (v < vFrom && nextVolume > v)) {
											setVolume(nextVolume, pActionId);
										} else {
											setVolume(v, pActionId);
										}
										pTimerHandler.reset();
									}
								}
							}
						}));
			}
		}
	}

	public void restoreVolumeWithFade(float t) {
		restoreVolumeWithFade(t, CALLBACK_EMPTY);
	}

	public void restoreVolumeWithFade(float t, Runnable pCallback) {
		final UUID actionId = generateNewActionId();
		generateDelayedAction(new Runnable() {
			@Override
			public void run() {
				restoreVolume(actionId);
			}
		}, pCallback);
		restoreVolumeWithFade(t, actionId);
	}

	private void restoreVolumeWithFade(float t, final UUID pActionId) {
		log("restoreVolumeWithFade(" + t + ")");
		if (isActionValid(pActionId)) {
			fadeVolume(default_volume, t, pActionId, null);
		}
	}

	public void restoreVolume() {
		final UUID actionId = generateNewActionId();
		generateDirectAction();
		restoreVolume(actionId);
	}

	private void restoreVolume(final UUID pActionId) {
		log("restoreVolume()");
		if (isActionValid(pActionId)) {
			setVolume(default_volume, pActionId);
		}
	}

	public void play() {
		final UUID actionId = generateNewActionId();
		generateDirectAction();
		play(actionId);
	}

	private void play(final UUID pActionId) {
		log("play()");
		if (isActionValid(pActionId)) {
			if (music != null) {
				directPlay();
				onActionFinished();
			}
		}
	}

	private void directPlay() {
		log("directPlay()");
		if (music != null && !music.isPlaying()) {
			if (!SettingsManager.getInstance().getMute()) {
				if (!ConstantVar.NO_MUSIC) {
					music.play();
				}
			}
		}
	}

	public void playFadeIn(float t) {
		playFadeIn(t, CALLBACK_EMPTY);
	}

	public void playFadeIn(float t, Runnable pCallback) {
		final UUID actionId = generateNewActionId();
		generateDelayedAction(new Runnable() {
			@Override
			public void run() {
				restoreVolume(actionId);
				play(actionId);
			}
		}, pCallback);
		playFadeIn(t, actionId);
	}

	private void playFadeIn(float t, final UUID pActionId) {
		log("playFadeIn(" + t + ")");
		if (isActionValid(pActionId)) {
			if (music != null) {
				if (!music.isPlaying()) {
					setVolume(0, pActionId);
					directPlay();
					fadeVolume(default_volume, t, pActionId, null);
				} else {
					fadeVolume(default_volume, t, pActionId, null);
				}
			}
		}
	}

	public void stop() {
		final UUID actionId = generateNewActionId();
		generateDirectAction();
		stop(actionId);
	}

	private void stop(final UUID pActionId) {
		log("stop()");
		if (isActionValid(pActionId)) {
			if (music != null) {
				if (music.isPlaying()) {
					music.pause();
					music.seekTo(0);
				}
				onActionFinished();
			}
		}
	}

	public void stopFadeOut(float t) {
		stopFadeOut(t, CALLBACK_EMPTY);
	}

	public void stopFadeOut(float t, Runnable pCallback) {
		final UUID actionId = generateNewActionId();
		generateDelayedAction(new Runnable() {
			@Override
			public void run() {
				stop(actionId);
			}
		}, pCallback);
		stopFadeOut(t, actionId);
	}

	private void stopFadeOut(float t, UUID pActionId) {
		log("stopFadeOut(" + t + ")");
		if (isActionValid(pActionId)) {
			if (music != null) {
				if (music.isPlaying()) {
					fadeVolume(0, t, pActionId, new CustomMusicCallback() {
						@Override
						public void onCompleted(UUID pActionId) {
							stop(pActionId);
							restoreVolume(pActionId);
						}
					});
				} else {
					onActionFinished();
				}
			}
		}
	}

	public void pause() {
		final UUID actionId = generateNewActionId();
		generateDirectAction();
		pause(actionId);
	}

	private void pause(final UUID pActionId) {
		log("pause()");
		if (isActionValid(pActionId)) {
			if (music != null) {
				if (music.isPlaying()) {
					music.pause();
				}
				onActionFinished();
			}
		}
	}

	public void pauseFadeOut(float t) {
		pauseFadeOut(t, CALLBACK_EMPTY);
	}

	public void pauseFadeOut(float t, Runnable pCallback) {
		final UUID actionId = generateNewActionId();
		generateDelayedAction(new Runnable() {
			@Override
			public void run() {
				pause(actionId);
			}
		}, pCallback);
		pauseFadeOut(t, actionId);
	}

	private void pauseFadeOut(float t, UUID pActionId) {
		log("pauseFadeOut(" + t + ")");
		if (isActionValid(pActionId)) {
			if (music != null) {
				if (music.isPlaying()) {
					fadeVolume(0, t, pActionId, new CustomMusicCallback() {
						@Override
						public void onCompleted(UUID pActionId) {
							pause(pActionId);
							restoreVolume(pActionId);
						}
					});
				} else {
					onActionFinished();
				}
			}
		}
	}
}
