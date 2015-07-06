package com.benjastudio.andengine.extension.comoncore.entity.effect;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.util.modifier.IModifier;

import com.benjastudio.andengine.extension.comoncore.scene.IBaseScene;

public class Rain extends Entity {

	IBaseScene<?, ?> scene;
	float n, fallingDownDuration;

	public Rain(IBaseScene<?, ?> pScene, float pNumberOfParticles,
			float pFallingDownDuration) {
		scene = pScene;
		n = pNumberOfParticles;
		fallingDownDuration = pFallingDownDuration;
		for (int i = 0; i < n; i++)
			createRaindParticle();
	}

	private void createRaindParticle() {
		createRaindParticle(scene.getSceneWidth() * (float) Math.random(),
				scene.getSceneHeight() * (float) Math.random());
	}

	private void createRaindParticle(final float pX, final float pY) {
		final Rectangle particle = new Rectangle(pX, 0, 2, 3,
				scene.eBundle.vbom);
		particle.setAlpha(0.3f + 0.7f * (float) Math.random());
		IEntityModifierListener listener = new IEntityModifierListener() {
			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier,
					IEntity pItem) {
			}

			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier,
					IEntity pItem) {
				particle.registerEntityModifier(new LoopEntityModifier(
						new MoveYModifier(fallingDownDuration, -5, 500)));
			}
		};
		particle.registerEntityModifier(new MoveYModifier(fallingDownDuration
				* (500 - pY) / 500, pY, 500, listener));
		attachChild(particle);
	}
}
