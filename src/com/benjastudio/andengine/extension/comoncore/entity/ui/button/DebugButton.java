package com.benjastudio.andengine.extension.comoncore.entity.ui.button;

import org.andengine.entity.sprite.ButtonSprite;

import com.benjastudio.andengine.extension.comoncore.IManagerBundle;
import com.benjastudio.andengine.extension.comoncore.SettingsManager;
import com.benjastudio.andengine.extension.comoncore.scene.IBaseScene;

public class DebugButton<T extends IManagerBundle<?, ?, ?, ?, ?, ?, ?>, TSceneType extends Enum<TSceneType>>
		extends CustomButtonSprite<T, TSceneType> {

	public DebugButton(IBaseScene<T, TSceneType> pScene) {
		super(128, 74 + 64,
				pScene.mBundle.getResourceManager().comon.iconDebug_TR, pScene,
				false, true, new OnClickListener() {

					@Override
					public void onClick(ButtonSprite pButtonSprite,
							float pTouchAreaLocalX, float pTouchAreaLocalY) {
						SettingsManager.getInstance().setDebug(
								!SettingsManager.getInstance().getDebug());
					}
				});
	}
}
