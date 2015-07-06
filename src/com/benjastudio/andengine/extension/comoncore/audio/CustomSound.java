package com.benjastudio.andengine.extension.comoncore.audio;

import org.andengine.audio.sound.Sound;

import com.benjastudio.andengine.extension.comoncore.SettingsManager;

public class CustomSound {
	public Sound sound;

	public void play() {
		if (!SettingsManager.getInstance().getMute()) {
			sound.play();
		}
	}
}
