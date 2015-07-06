package com.benjastudio.andengine.extension.comoncore.scene;

import java.util.HashMap;

import org.andengine.util.adt.queue.CircularQueue;

import android.util.Log;

import com.benjastudio.andengine.extension.comoncore.EngineBundle;
import com.benjastudio.andengine.extension.comoncore.IManagerBundle;
import com.benjastudio.andengine.extension.comoncore.SettingsManager;

public abstract class ISceneManager<T extends IManagerBundle<?, ?, ?, ?, ?, ?, ?>, TSceneType extends Enum<TSceneType>> {

	static final private String TAG = "ISceneManager";
	CircularQueue<TSceneType> previousSceneTypes = new CircularQueue<TSceneType>(
			5);
	protected HashMap<TSceneType, IBaseScene<T, TSceneType>> scenes = new HashMap<TSceneType, IBaseScene<T, TSceneType>>();

	protected EngineBundle eBundle;
	protected T mBundle;

	private IBaseScene<T, TSceneType> currentScene;

	public abstract IBaseScene<T, TSceneType> createScene(TSceneType sceneType);

	public abstract void onSceneSet(TSceneType sceneType);

	public abstract TSceneType getStartingSceneType();

	public abstract TSceneType getHomeSceneType();

	public ISceneManager(EngineBundle pEBundle, T pMBundle) {
		eBundle = pEBundle;
		mBundle = pMBundle;
	}

	public IBaseScene<T, TSceneType> getCurrentScene() {
		return currentScene;
	}

	public TSceneType getCurrentTypeScene() {
		if (currentScene == null)
			return null;
		return currentScene.getSceneType();
	}

	public void setScene(TSceneType sceneType) {
		setScene(sceneType, true);
	}

	public void setScene(TSceneType sceneType, boolean recordPreviousScene) {
		Log.d(TAG, "setScene(" + sceneType + ", " + recordPreviousScene + ")");
		if (sceneType == null)
			sceneType = getStartingSceneType();
		IBaseScene<T, TSceneType> scene = scenes.get(sceneType);
		if (scene == null) {
			scene = createScene(sceneType);
			if (scene == null) {
				Log.e(TAG, "scene is null");
			}
			scenes.put(sceneType, scene);
		}
		scene.dispose();
		scene.prepare();
		setScene(scene, recordPreviousScene);
		onSceneSet(sceneType);
		Log.d(TAG, "Scene (" + sceneType + ") is set.");
	}

	private void onSceneChange(IBaseScene<T, TSceneType> fromScene,
			IBaseScene<T, TSceneType> toScene) {
		Log.d(TAG,
				"onSceneChange("
						+ ((fromScene == null) ? "null" : fromScene
								.getSceneType()) + ", "
						+ ((toScene == null) ? "null" : toScene.getSceneType())
						+ ")");
		if (eBundle.musicManager != null) {
			if (fromScene == null) {
				eBundle.musicManager.onSceneChange(null, toScene.getMusic());
			} else {
				eBundle.musicManager.onSceneChange(fromScene.getMusic(),
						toScene.getMusic());
			}
		}
	}

	public void goToPreviousScene() {
		TSceneType previousSceneType = previousSceneTypes.removeLast();
		if (previousSceneType != null)
			setScene(previousSceneType, false);
		else
			setScene(getStartingSceneType(), false);
	}

	public void onSubSceneAttached(ISubScene<T, TSceneType> subScene) {
		Log.d(TAG, "onSubSceneAttached(" + subScene.getSceneType() + ")");
		onSceneChange(currentScene, subScene);
	}

	public void onSubSceneDetattached(ISubScene<T, TSceneType> subScene,
			IBaseScene<T, TSceneType> parentScene) {
		Log.d(TAG, "onSubSceneDetattached(" + subScene.getSceneType() + ", "
				+ parentScene.getSceneType() + ")");
		onSceneChange(subScene, parentScene);
	}

	private void setScene(IBaseScene<T, TSceneType> baseScene,
			boolean recordPreviousScene) {

		if (recordPreviousScene) {
			if (currentScene != null)
				previousSceneTypes.add(currentScene.getSceneType());
			else
				previousSceneTypes.add(getStartingSceneType());
		}

		if (baseScene != null) {
			Log.d(TAG, "setScene(" + baseScene.getSceneType() + ")");
			if (currentScene != null) {
				currentScene.onStop();
			}
			onSceneChange(currentScene, baseScene);
			eBundle.engine.setScene(baseScene);
			currentScene = baseScene;
			currentScene.onResume();
		} else {
			Log.d(TAG, "setScene(null)");
			eBundle.engine.setScene(null);
		}
	}

	protected void showStorySceneIfNeverViewed() {
		Log.d(TAG, "showStorySceneIfNeverViewed()");
		if (!SettingsManager.getInstance()
				.isStoryViewedForCurrentSelectedWorld()) {
			mBundle.getStoryLevelManager().playFirstLevelForThisWorld();
			SettingsManager.getInstance()
					.setStoryViewedForCurrentSelectedWorld(true);
		}
	}
}
