package com.benjastudio.andengine.extension.comoncore.scene;

import com.benjastudio.andengine.extension.comoncore.EngineBundle;
import com.benjastudio.andengine.extension.comoncore.IManagerBundle;

import android.util.Log;

public abstract class ISubScene<T extends IManagerBundle, TSceneType extends Enum<TSceneType>>
		extends IBaseScene<T, TSceneType> {

	private static final String TAG = "ISubScene";

	private IBaseScene<T, TSceneType> parentScene;

	public ISubScene(EngineBundle pEBundle, T mPBundle) {
		super(pEBundle, mPBundle);
	}

	public ISubScene(IBaseScene<T, TSceneType> pParentScene) {
		this(pParentScene, true);
	}

	public ISubScene(IBaseScene<T, TSceneType> pParentScene, boolean prepare) {
		super(pParentScene.eBundle, pParentScene.mBundle);
		setParentScene(pParentScene);
		setBackgroundEnabled(false);
		if (prepare)
			prepare();
	}

	public void setParentScene(IBaseScene<T, TSceneType> pParentScene) {
		parentScene = pParentScene;
	}

	public void back() {
		Log.d(TAG, "back");
		if (parentScene != null) {
			parentScene.clearChildScene();
			parentScene.onResumeFromSubScene(ISubScene.this);
			dispose();
		} else if (parentScene.equals(ISubScene.this)) {
			Log.e(TAG, "Can't back, parent scene is this scene.");
		} else {
			Log.e(TAG, "Can't back, parent scene is not set.");
		}
	}

	public IBaseScene<T, TSceneType> getParentScene() {
		return parentScene;
	}

	public abstract boolean pauseParentScene();
}
