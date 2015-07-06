package com.benjastudio.andengine.extension.comoncore.entity.ui.button;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.input.touch.TouchEvent;

import com.benjastudio.andengine.extension.comoncore.IManagerBundle;
import com.benjastudio.andengine.extension.comoncore.SettingsManager;
import com.benjastudio.andengine.extension.comoncore.scene.IBaseScene;

public class MuteButton<T extends IManagerBundle<?, ?, ?, ?, ?, ?, ?>, TSceneType extends Enum<TSceneType>>
		extends AnimatedSprite {

	public MuteButton(IBaseScene<T, TSceneType> scene) {
		super(595, 10, scene.mBundle.getResourceManager().comon.iconMute_TTR,
				scene.eBundle.vbom);
		setCurrentTileIndex(SettingsManager.getInstance().getMute() ? 1 : 0);
		setScale(0.8f);
		scene.registerTouchArea(this);
		scene.iTouchAreaList.add(this);
	}

	@Override
	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
			final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		switch (pSceneTouchEvent.getAction()) {
		case TouchEvent.ACTION_DOWN:
			SettingsManager.getInstance().setMute(
					!SettingsManager.getInstance().getMute());
			setCurrentTileIndex(SettingsManager.getInstance().getMute() ? 1 : 0);
			break;
		default:
			break;
		}
		return true;
	}
}