package com.benjastudio.andengine.extension.comoncore.entity.effect;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.adt.pool.GenericPool;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.ease.EaseSineIn;

import android.opengl.GLES20;

import com.benjastudio.andengine.extension.comoncore.EngineBundle;

public class ExplosionFactory {

	GenericPool<Sprite> firePool;

	private float duration = 0.5f;
	private float distance = 100f;
	private int n = 20;
	EngineBundle eBundle;
	int blendSrc, blendDst;
	float fromAlpha, toAlpha;

	public ExplosionFactory(final EngineBundle pEBundle,
			final ITextureRegion textureRegion, float pDuration,
			float pDistance, int pN) {
		eBundle = pEBundle;
		duration = pDuration;
		distance = pDistance;
		n = pN;
		setBlendColored();
		firePool = new GenericPool<Sprite>(n) {

			@Override
			protected Sprite onAllocatePoolItem() {
				Sprite sprite = new Sprite(0, 0, textureRegion, eBundle.vbom);
				sprite.setVisible(false);
				return sprite;
			}

			@Override
			protected void onHandleObtainItem(Sprite pItem) {
				super.onHandleObtainItem(pItem);
				pItem.reset();
				pItem.setVisible(false);
			}

			@Override
			protected void onHandleRecycleItem(Sprite pItem) {
				super.onHandleRecycleItem(pItem);
				pItem.setVisible(false);
				pItem.setIgnoreUpdate(true);
			}
		};
	}

	public void setBlendColored() {
		blendSrc = GLES20.GL_ONE_MINUS_SRC_ALPHA;
		blendDst = GLES20.GL_ONE;
		fromAlpha = 0;
		toAlpha = 1;
	}

	public void setBlendNeutral() {
		blendSrc = GLES20.GL_SRC_ALPHA;
		blendDst = GLES20.GL_ONE_MINUS_SRC_ALPHA;
		fromAlpha = 1;
		toAlpha = 0;
	}

	public void createExplosion(IEntity parentEntity, float x, float y) {

		for (int i = 0; i < n; i++) {

			final Sprite fireParticle = firePool.obtainPoolItem();
			parentEntity.attachChild(fireParticle);
			final float rotation = 360f * (float) Math.random();
			final float rad = (float) (2 * Math.PI * Math.random());
			final float xOffset = distance * (float) Math.cos(rad * i);
			final float yOffset = distance * (float) Math.sin(rad * i);
			fireParticle.setBlendFunction(blendSrc, blendDst);
			fireParticle.registerEntityModifier(new AlphaModifier(duration,
					fromAlpha, toAlpha));
			fireParticle.registerEntityModifier(new RotationModifier(duration,
					rotation, rotation + 360));
			fireParticle.registerEntityModifier(new MoveModifier(duration, x, x
					+ xOffset, y, y + yOffset, EaseSineIn.getInstance()));
			fireParticle.registerEntityModifier(new ScaleModifier(duration,
					0.5f, 2f, new IEntityModifierListener() {
						@Override
						public void onModifierStarted(
								IModifier<IEntity> pModifier, IEntity pItem) {
							fireParticle.setVisible(true);
						}

						@Override
						public void onModifierFinished(
								IModifier<IEntity> pModifier,
								final IEntity pItem) {
							eBundle.engine.runOnUpdateThread(new Runnable() {
								@Override
								public void run() {
									firePool.recyclePoolItem(fireParticle);
									if (pItem.hasParent())
										pItem.detachSelf();
								}
							});
						}
					}));
		}
	}
}
