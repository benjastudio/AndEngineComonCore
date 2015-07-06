package com.benjastudio.andengine.extension.comoncore.entity.ui.button;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.ease.EaseQuadInOut;

import com.benjastudio.andengine.extension.comoncore.IManagerBundle;
import com.benjastudio.andengine.extension.comoncore.scene.IBaseScene;
import com.benjastudio.andengine.extension.comoncore.scene.SceneObserver;

public class CustomButtonSprite<T extends IManagerBundle<?, ?, ?, ?, ?, ?, ?>, TSceneType extends Enum<TSceneType>>
		extends ButtonSprite implements SceneObserver {

	private boolean moveEnabled;
	IBaseScene<T, TSceneType> scene;
	public Text text;

	public CustomButtonSprite(float pX, float pY,
			final ITiledTextureRegion pTiledTextureRegion,
			final IBaseScene<T, TSceneType> pScene, boolean pMove,
			boolean pSound, final OnClickListener pOnClickListener) {
		super(pX, pY, pTiledTextureRegion, pScene.eBundle.vbom, null);
		init(pScene, pMove, pSound, pOnClickListener);
		scene = pScene;
	}

	public CustomButtonSprite(float pX, float pY,
			final ITextureRegion pTextureRegion,
			final IBaseScene<T, TSceneType> pScene, boolean pMove,
			boolean pSound, final OnClickListener pOnClickListener) {
		super(pX, pY, pTextureRegion, pScene.eBundle.vbom, null);
		init(pScene, pMove, pSound, pOnClickListener);
	}

	public void init(final IBaseScene<T, TSceneType> pScene, boolean pMove,
			boolean pSound, final OnClickListener pOnClickListener) {
		pScene.registerTouchArea(CustomButtonSprite.this);
		if (pMove)
			enableMove();
		else
			disableMove();
		setPosition(getX() - getWidth() * 0.5f, getY() - getHeight() - 0.5f);
		if (pSound) {
			OnClickListener onClickListenerPlusSound = new OnClickListener() {
				@Override
				public void onClick(ButtonSprite pButtonSprite,
						float pTouchAreaLocalX, float pTouchAreaLocalY) {
					pOnClickListener.onClick(pButtonSprite, pTouchAreaLocalX,
							pTouchAreaLocalY);
					pScene.mBundle.getResourceManager().comon.clickSound.play();
				}
			};
			setOnClickListener(onClickListenerPlusSound);
		} else
			setOnClickListener(pOnClickListener);
	}

	public void addIcon(RectangularShape pRectangularShape) {
		pRectangularShape.setPosition(
				getWidth() * 0.5f - pRectangularShape.getWidth() * 0.5f,
				getHeight() * 0.5f - pRectangularShape.getHeight() * 0.5f);
		attachChild(pRectangularShape);
	}

	public void addText(String str) {
		text = new Text(0, 0, scene.mBundle.getResourceManager().comon.font,
				str, scene.eBundle.vbom);
		addIcon(text);
	}

	public void disableMove() {
		moveEnabled = false;
	}

	public void enableMove() {
		moveEnabled = true;
		move();
	}

	public boolean isMoveEnabled() {
		return moveEnabled;
	}

	public void move() {

		if (!isMoveEnabled())
			return;

		final float fromScaleX = getScaleX();
		final float toScaleX = fromScaleX * 0.92f;
		final float fromScaleY = getScaleY();
		final float toScaleY = fromScaleY * 0.92f;

		registerEntityModifier(new SequenceEntityModifier(new ScaleModifier(1f,
				fromScaleX, toScaleX, fromScaleY, toScaleY,
				EaseQuadInOut.getInstance()), new ScaleModifier(1f, toScaleX,
				fromScaleX, toScaleY, fromScaleY,
				new IEntityModifierListener() {
					@Override
					public void onModifierStarted(IModifier<IEntity> pModifier,
							IEntity pItem) {
					}

					@Override
					public void onModifierFinished(
							IModifier<IEntity> pModifier, IEntity pItem) {
						if (isMoveEnabled())
							move();
					}
				}, EaseQuadInOut.getInstance())));
	}

	@Override
	public void onSceneDispose(IBaseScene<?, ?> pScene) {
		pScene.unregisterTouchArea(CustomButtonSprite.this);
	}

	@Override
	public void onScenePrepare(IBaseScene<?, ?> pScene) {
	}

	@Override
	public void onSceneTimePreElapsed(IBaseScene<?, ?> pScene) {
	}

	@Override
	public void onSceneTimeElapsed(IBaseScene<?, ?> pScene) {
	}
}
