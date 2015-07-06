package com.benjastudio.andengine.extension.comoncore.scene;

public interface SceneObservable {
	public void addSceneObserver(SceneObserver object);

	public void removeSceneObserver(SceneObserver object);

	public void notifySceneObserversOnPrepare();

	public void notifySceneObserversOnDispose();

	public void notifySceneObserversOnTimePreElapsed();

	public void notifySceneObserversOnTimeElapsed();
}
