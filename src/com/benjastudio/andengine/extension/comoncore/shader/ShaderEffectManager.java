package com.benjastudio.andengine.extension.comoncore.shader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.UncoloredSprite;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.texture.PixelFormat;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.texture.render.RenderTexture;
import org.andengine.opengl.util.GLState;
import org.andengine.util.color.Color;
import org.andengine.util.debug.Debug;

import com.benjastudio.andengine.extension.comoncore.EngineBundle;

import android.annotation.SuppressLint;
import android.opengl.GLES20;

@SuppressLint("WrongCall")
public class ShaderEffectManager {

	EngineBundle eBundle;

	float time = 0;
	private static ShaderEffectManager instance;
	private ShaderProgram savedShaderProgram;

	public boolean frameBufferSuported;
	public boolean initialized;

	IUpdateHandler timeUpdateHandler;

	// renderTextures(0)/renderTextureSprites(0) are render/sprite input/output
	// for all pass.
	private List<RenderTexture> renderTextures = new ArrayList<RenderTexture>();
	private List<Sprite> renderTextureSprites = new ArrayList<Sprite>();

	/* Shader effects */
	// Transition effect
	private TransitionShaderEffect transitionShaderEffect = new TransitionShaderEffect();
	// Radial blur effect
	private RadialBlurShaderEffect radialBlurShaderEffect = new RadialBlurShaderEffect();
	// Shock-waves effect - Synchronized -> prevent concurrent modification
	// exceptions
	Collection<ShockwaveShaderEffect> synchronizedShockwaves = Collections
			.synchronizedList(new ArrayList<ShockwaveShaderEffect>());

	private ShaderEffectManager() {
		initialized = false;
		frameBufferSuported = true;
	}

	public boolean isFramebufferSupported(GLState pGLState) {
		return pGLState.getFramebufferStatus() != GLES20.GL_FRAMEBUFFER_UNSUPPORTED;
	}

	public void init(EngineBundle pEBundle, GLState pGLState) {

		frameBufferSuported = isFramebufferSupported(pGLState);
		if (!frameBufferSuported)
			return;

		if (initialized == false) {
			eBundle = pEBundle;

			eBundle.engine.getShaderProgramManager().loadShaderProgram(
					TransitionShaderProgram.getInstance());
			eBundle.engine.getShaderProgramManager().loadShaderProgram(
					ShockwaveShaderProgram.getInstance());
			eBundle.engine.getShaderProgramManager().loadShaderProgram(
					RadialBlurShaderProgram.getInstance());
			eBundle.engine.getShaderProgramManager().loadShaderProgram(
					BlurShaderProgram.getInstance());

			// clear old
			renderTextures.clear();
			renderTextureSprites.clear();
			if (timeUpdateHandler != null) {
				eBundle.engine.unregisterUpdateHandler(timeUpdateHandler);
				timeUpdateHandler = null;
			}

			for (int i = 0; i < 3; i++) {
				renderTextures.add(new RenderTexture(eBundle.engine
						.getTextureManager(), eBundle.camera.getSurfaceWidth(),
						eBundle.camera.getSurfaceHeight(),
						PixelFormat.RGBA_4444));
				try {
					renderTextures.get(i).init(pGLState);
				} catch (Exception e) {
					frameBufferSuported = false;
					return;
				}
				renderTextureSprites.add(new UncoloredSprite(0f, 0f,
						TextureRegionFactory.extractFromTexture(renderTextures
								.get(i)), eBundle.vbom));
			}
			renderTextureSprites.get(2).setShaderProgram(
					BlurShaderProgram.getInstance());

			timeUpdateHandler = new IUpdateHandler() {
				@Override
				public void reset() {
				}

				@Override
				public void onUpdate(float pSecondsElapsed) {
					time += pSecondsElapsed;
					// reset time after 20minutes if there are no shader alive.
					if (time > 1200f && !hasShadersToCompute()) {
						time = 0f;
					}
				}
			};
			eBundle.engine.registerUpdateHandler(timeUpdateHandler);

			Debug.v("ShaderEffectManager: init() done");
			initialized = true;
		}
	}

	public boolean hasShadersToCompute() {
		if (radialBlurShaderEffect.isActive)
			return true;
		if (transitionShaderEffect.isActive)
			return true;
		if (!synchronizedShockwaves.isEmpty())
			return true;
		return false;
	}

	public static ShaderEffectManager getInstance() {
		if (instance == null)
			instance = new ShaderEffectManager();
		return instance;
	}

	public RenderTexture getRenderTextureInputOutput() {
		return renderTextures.get(0);
	}

	public Sprite getRenderTextureSpriteInputOutput() {
		return renderTextureSprites.get(0);
	}

	public RenderTexture getRenderTextureOutline() {
		return renderTextures.get(2);
	}

	public Sprite getRenderTextureSpriteInputOutline() {
		return renderTextureSprites.get(2);
	}

	public void addShockwaveEffet(final float pX, final float pY) {
		Debug.w("addShockwaveEffet(" + pX + "," + pY + ")");
		synchronizedShockwaves.add(new ShockwaveShaderEffect(pX, pY, time));
	}

	public RenderTexture getRenderTexture(final int i) {
		return renderTextures.get(i);
	}

	public Sprite getRenderTextureSprite(final int i) {
		return renderTextureSprites.get(i);
	}

	public void swapRendersAndSprites() {
		Collections.swap(renderTextures, 0, 1);
		Collections.swap(renderTextureSprites, 0, 1);
	}

	public void addTransitionEffect(final float pX, final float pY,
			final float duration) {
		Debug.v("ShaderEffectMaanger: addTransitionEffect(" + pX + "," + pY
				+ "," + duration + ")");
		transitionShaderEffect.x = pX;
		transitionShaderEffect.y = pY;
		transitionShaderEffect.t = time;
		transitionShaderEffect.duration = duration;
		transitionShaderEffect.isActive = true;
	}

	public void removeTransition() {
		transitionShaderEffect.isActive = false;
	}

	public void addRadialBlurEffect(final float pX, final float pY,
			final float duration, final float distance, final float strength) {
		Debug.v("ShaderEffectMaanger: addRadialBlurEffect(" + pX + "," + pY
				+ "," + duration + ")");
		radialBlurShaderEffect.x = pX;
		radialBlurShaderEffect.y = pY;
		radialBlurShaderEffect.t = time;
		radialBlurShaderEffect.duration = duration; // -1 -> inf.
		radialBlurShaderEffect.distance = distance;
		radialBlurShaderEffect.strength = strength;
		radialBlurShaderEffect.isActive = true;
	}

	public void changeRadialBlurEffectCenterPosition(final float pX,
			final float pY) {
		radialBlurShaderEffect.x = pX;
		radialBlurShaderEffect.y = pY;
	}

	public void changeRadialBlurEffectParams(final float distance,
			final float strength) {
		radialBlurShaderEffect.distance = distance;
		radialBlurShaderEffect.strength = strength;
	}

	public void removeRadialBlur() {
		radialBlurShaderEffect.isActive = false;
	}

	public void saveShaderPrograms() {
		savedShaderProgram = getRenderTextureSprite(0).getShaderProgram();
	}

	public void restoreShaderPrograms() {
		getRenderTextureSprite(0).setShaderProgram(savedShaderProgram);
		getRenderTextureSprite(1).setShaderProgram(savedShaderProgram);
	}

	public void setShaderPrograms(ShaderProgram shaderProgram) {
		getRenderTextureSprite(0).setShaderProgram(shaderProgram);
		getRenderTextureSprite(1).setShaderProgram(shaderProgram);
	}

	public void applyTransitionPass(GLState pGLState, Camera pCamera) {

		if (!transitionShaderEffect.isActive)
			return;
		if ((time - transitionShaderEffect.t > transitionShaderEffect.duration)
				&& (transitionShaderEffect.duration != -1)) {
			transitionShaderEffect.isActive = false;
			return;
		}

		saveShaderPrograms();
		setShaderPrograms(TransitionShaderProgram.getInstance());

		// 0 -> 1
		getRenderTexture(1).begin(pGLState, false, true, Color.TRANSPARENT);
		{
			getRenderTextureSprite(0).onDraw(pGLState, pCamera);
			GLES20.glUniform2f(TransitionShaderProgram.sUniformParamsLocation,
					time - transitionShaderEffect.t,
					transitionShaderEffect.duration);
			GLES20.glUniform2f(TransitionShaderProgram.sUniformCenterLocation,
					transitionShaderEffect.x / 800,
					1f - (transitionShaderEffect.y / 480));
		}
		getRenderTexture(1).end(pGLState);

		// 1 -> 0
		swapRendersAndSprites();
		restoreShaderPrograms();
	}

	public void applyRadialBlurPass(GLState pGLState, Camera pCamera) {

		if (!radialBlurShaderEffect.isActive)
			return;
		if ((time - radialBlurShaderEffect.t > radialBlurShaderEffect.duration)
				&& (radialBlurShaderEffect.duration != -1)) {
			radialBlurShaderEffect.isActive = false;
			return;
		}

		saveShaderPrograms();
		setShaderPrograms(RadialBlurShaderProgram.getInstance());

		// 0 -> 1
		getRenderTexture(1).begin(pGLState, false, true, Color.TRANSPARENT);
		{
			getRenderTextureSprite(0).onDraw(pGLState, pCamera);
			GLES20.glUniform2f(
					RadialBlurShaderProgram.sUniformRadialBlurCenterLocation,
					radialBlurShaderEffect.x / 800f,
					1f - (radialBlurShaderEffect.y / 480f));
			GLES20.glUniform2f(RadialBlurShaderProgram.sUniformParams,
					radialBlurShaderEffect.distance,
					radialBlurShaderEffect.strength);
		}
		getRenderTexture(1).end(pGLState);

		// 1 -> 0
		swapRendersAndSprites();
		restoreShaderPrograms();
	}

	public void applyShockwavePass(GLState pGLState, Camera pCamera) {

		// No work to do here.
		if (synchronizedShockwaves.isEmpty())
			return;

		saveShaderPrograms();
		setShaderPrograms(ShockwaveShaderProgram.getInstance());

		// Remove old shock-waves
		synchronized (synchronizedShockwaves) {
			for (Iterator<ShockwaveShaderEffect> itr = synchronizedShockwaves
					.iterator(); itr.hasNext();) {
				ShockwaveShaderEffect shockwave = itr.next();
				if (time - shockwave.t > 2.5f) {
					itr.remove();
				}
			}
		}

		// Synchronized -> prevent concurrent modification exceptions
		ShockwaveShaderEffect[] shockwavesArray = synchronizedShockwaves
				.toArray(new ShockwaveShaderEffect[synchronizedShockwaves
						.size()]);

		int iCurrent = -1;
		int iNext = -1;
		for (ShockwaveShaderEffect shockwave : shockwavesArray) {
			iCurrent = (iCurrent != 0) ? 0 : 1; // alternate iCurrent=0,
												// iCurrent=1, iCurrent=0, ...
			iNext = (iCurrent == 0) ? 1 : 0; // alternate iNext=1, iNext=0,
												// iNext=1, ...

			getRenderTexture(iNext).begin(pGLState, false, true,
					Color.TRANSPARENT);
			{
				getRenderTextureSprite(iCurrent).onDraw(pGLState, pCamera);
				GLES20.glUniform1f(ShockwaveShaderProgram.sUniformTimeLocation,
						time - shockwave.t);
				GLES20.glUniform2f(
						ShockwaveShaderProgram.sUniformCenterLocation,
						shockwave.x / 800, 1f - (shockwave.y / 480));
			}
			getRenderTexture(iNext).end(pGLState);
		}

		// Swap render/sprite in order to have 0 in output
		if (iNext != 0)
			swapRendersAndSprites();
		restoreShaderPrograms();
	}

	public void applyBlurPass(GLState pGLState, Camera pCamera) {

		saveShaderPrograms();
		setShaderPrograms(BlurShaderProgram.getInstance());

		// 0 -> 1
		getRenderTexture(1).begin(pGLState, false, true, Color.TRANSPARENT);
		{
			getRenderTextureSprite(0).onDraw(pGLState, pCamera);
			GLES20.glUniform2f(
					RadialBlurShaderProgram.sUniformRadialBlurCenterLocation,
					radialBlurShaderEffect.x / 800f,
					1f - (radialBlurShaderEffect.y / 480f));
			GLES20.glUniform2f(RadialBlurShaderProgram.sUniformParams,
					radialBlurShaderEffect.distance,
					radialBlurShaderEffect.strength);
		}
		getRenderTexture(1).end(pGLState);

		// 1 -> 0
		swapRendersAndSprites();
		restoreShaderPrograms();
	}
}
