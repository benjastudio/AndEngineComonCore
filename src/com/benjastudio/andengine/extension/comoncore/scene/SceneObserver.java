package com.benjastudio.andengine.extension.comoncore.scene;

public interface SceneObserver {
	public void onSceneDispose(IBaseScene<?, ?> pScene);

	public void onScenePrepare(IBaseScene<?, ?> pScene);

	public void onSceneTimePreElapsed(IBaseScene<?, ?> pScene);

	public void onSceneTimeElapsed(IBaseScene<?, ?> pScene);
}
