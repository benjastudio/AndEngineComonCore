package com.benjastudio.andengine.extension.comoncore.entity.ui.button;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;

import com.benjastudio.andengine.extension.comoncore.IManagerBundle;
import com.benjastudio.andengine.extension.comoncore.scene.IBaseScene;

public class RectangularLongButtonSprite<T extends IManagerBundle<?, ?, ?, ?, ?, ?, ?>, TSceneType extends Enum<TSceneType>>
		extends CustomButtonSprite<T, TSceneType> {

	public RectangularLongButtonSprite(float pX, float pY,
			ITextureRegion pIcon, IBaseScene<T, TSceneType> pScene,
			boolean pMove, boolean pSound, OnClickListener pOnClickListener) {
		super(
				pX,
				pY,
				pScene.mBundle.getResourceManager().comon.buttonRectangularLong_TTR,
				pScene, pMove, pSound, pOnClickListener);
		if (pIcon != null)
			addIcon(new Sprite(0, 0, pIcon, pScene.eBundle.vbom));
	}

	public RectangularLongButtonSprite(float pX, float pY, String text,
			IBaseScene<T, TSceneType> pScene, boolean pMove, boolean pSound,
			OnClickListener pOnClickListener) {
		super(
				pX,
				pY,
				pScene.mBundle.getResourceManager().comon.buttonRectangularLong_TTR,
				pScene, pMove, pSound, pOnClickListener);
		if (text != null)
			addText(text);
	}
}
