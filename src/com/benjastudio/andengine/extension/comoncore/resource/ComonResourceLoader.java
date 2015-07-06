package com.benjastudio.andengine.extension.comoncore.resource;

import java.io.IOException;

import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.util.color.Color;

import android.app.Activity;
import android.util.Log;

import com.benjastudio.andengine.extension.comoncore.audio.CustomSound;
import com.benjastudio.andengine.extension.comoncore.audio.MusicManager;

public class ComonResourceLoader extends ResourceLoader {

	public Font font;

	private BuildableBitmapTextureAtlas buttonMenuBuidableAtlas;
	public ITiledTextureRegion buttonRounded_TTR;
	public ITiledTextureRegion buttonRectangular_TTR;
	public ITiledTextureRegion buttonRectangularLong_TTR;

	private BuildableBitmapTextureAtlas iconBuidableAtlas;
	public ITiledTextureRegion iconMute_TTR;
	public ITiledTextureRegion iconVibrator_TTR;
	public ITextureRegion iconDebug_TR;
	public ITextureRegion iconTime_TR;
	public ITextureRegion iconExit_TR;
	public ITextureRegion iconHome_TR;
	public ITextureRegion iconRestart_TR;
	public ITextureRegion iconPlay_TR;
	public ITextureRegion iconPause_TR;
	public ITextureRegion iconHelp_TR;
	public ITextureRegion iconLeft_TR;
	public ITextureRegion iconRight_TR;
	public ITextureRegion iconI_TR;
	public ITextureRegion iconFacebook_TR;
	public ITextureRegion iconNextLevel_TR;
	public ITextureRegion iconEye_TR;
	public ITextureRegion iconLock_TR;
	public ITextureRegion iconSettings_TR;

	public CustomSound clickSound = new CustomSound();

	public ComonResourceLoader(Activity pActivity, Engine pEngine,
			MusicManager pMusicManager, String pName) {
		super(pActivity, pEngine, pMusicManager, pName);
	}

	@Override
	protected void loadFonts(MultipleProgressListener pMultipleProgressListener) {

		FontFactory.setAssetBasePath("font/");
		final ITexture mainFontTexture = new BitmapTextureAtlas(
				engine.getTextureManager(), 512, 512,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		font = FontFactory.createStrokeFromAsset(engine.getFontManager(),
				mainFontTexture, activity.getAssets(), "font.ttf", 50.f, true,
				Color.WHITE.getABGRPackedInt(), 2.f,
				Color.BLACK.getABGRPackedInt());
		font.load();

		pMultipleProgressListener.changeProgress(100);
	}

	@Override
	protected void unloadFonts() {
		font.unload();
	}

	@Override
	protected void loadGraphics(
			MultipleProgressListener pMultipleProgressListener) {
	}

	@Override
	protected void unloadGraphics() {
	}

	private void loadIconInterface() {
		String basePath = "gfx/ui/";
		iconBuidableAtlas = new BuildableBitmapTextureAtlas(
				engine.getTextureManager(), 1024, 1024, textureQualityOptions);

		iconTime_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				iconBuidableAtlas, activity, basePath + "time.png");
		iconRight_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				iconBuidableAtlas, activity, basePath + "right.png");
		iconLeft_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				iconBuidableAtlas, activity, basePath + "left.png");
		iconHelp_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				iconBuidableAtlas, activity, basePath + "help.png");
		iconExit_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				iconBuidableAtlas, activity, basePath + "exit.png");
		iconHome_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				iconBuidableAtlas, activity, basePath + "home.png");
		iconRestart_TR = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(iconBuidableAtlas, activity, basePath
						+ "restart.png");
		iconPause_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				iconBuidableAtlas, activity, basePath + "iconPause.png");
		iconPlay_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				iconBuidableAtlas, activity, basePath + "iconPlay.png");
		iconI_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				iconBuidableAtlas, activity, basePath + "icon_i.png");
		iconFacebook_TR = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(iconBuidableAtlas, activity, basePath
						+ "facebook.png");
		iconNextLevel_TR = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(iconBuidableAtlas, activity, basePath
						+ "nextLevel.png");
		iconDebug_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				iconBuidableAtlas, activity, basePath + "iconDebug.png");
		iconEye_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				iconBuidableAtlas, activity, basePath + "iconEye.png");
		iconMute_TTR = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(iconBuidableAtlas, activity, basePath
						+ "iconMute.png", 2, 1);
		iconVibrator_TTR = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(iconBuidableAtlas, activity, basePath
						+ "iconVibrator.png", 2, 1);
		iconLock_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				iconBuidableAtlas, activity, basePath + "iconLock.png");
		iconSettings_TR = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(iconBuidableAtlas, activity, basePath
						+ "iconSettings.png");
		try {
			ITextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas> textureAtlasBuilder = new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
					2, 2, 2);
			iconBuidableAtlas.build(textureAtlasBuilder);
			iconBuidableAtlas.load();
		} catch (TextureAtlasBuilderException e) {
			e.printStackTrace();
		}
	}

	private void loadButtonInterface() {
		String basePath = "gfx/ui/";
		buttonMenuBuidableAtlas = new BuildableBitmapTextureAtlas(
				engine.getTextureManager(), 512, 512,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		buttonRounded_TTR = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(buttonMenuBuidableAtlas, activity,
						basePath + "base_button_rounded.png", 2, 1);
		buttonRectangular_TTR = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(buttonMenuBuidableAtlas, activity,
						basePath + "base_button_rectangular.png", 2, 1);
		buttonRectangularLong_TTR = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(buttonMenuBuidableAtlas, activity,
						basePath + "base_button_long_rectangular.png", 1, 2);

		try {
			ITextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas> textureAtlasBuilder = new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
					2, 2, 2);
			buttonMenuBuidableAtlas.build(textureAtlasBuilder);
			buttonMenuBuidableAtlas.load();
		} catch (TextureAtlasBuilderException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void loadInterface(
			MultipleProgressListener pMultipleProgressListener) {

		loadIconInterface();

		pMultipleProgressListener.changeProgress(50);

		loadButtonInterface();

		pMultipleProgressListener.changeProgress(100);
	}

	@Override
	protected void unloadInterface() {
		unloadAtlas(iconBuidableAtlas);
		unloadAtlas(buttonMenuBuidableAtlas);
	}

	@Override
	protected void loadSounds(MultipleProgressListener pMultipleProgressListener) {
		SoundFactory.setAssetBasePath("");
		try {

			clickSound.sound = SoundFactory.createSoundFromAsset(
					engine.getSoundManager(), activity, "mfx/sounds/click.ogg");
			clickSound.sound.setVolume(1f);

			pMultipleProgressListener.changeProgress(100);

		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void refresh() {
		Log.d(TAG, name + " refresh() (overridden)");
	}

	@Override
	protected void unloadSounds() {
	}

	@Override
	protected void loadMusics(MultipleProgressListener pMultipleProgressListener) {
	}

	@Override
	protected void unloadMusics() {
	}

	@Override
	protected void loadUnclassified(
			MultipleProgressListener pMultipleProgressListener) {
	}

	@Override
	protected void unloadUnclassified() {
	}
}