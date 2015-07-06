package com.benjastudio.andengine.extension.comoncore;

import com.benjastudio.andengine.extension.comoncore.facebook.IFacebookManager;
import com.benjastudio.andengine.extension.comoncore.level.ILevelManager;
import com.benjastudio.andengine.extension.comoncore.resource.IResourceManager;
import com.benjastudio.andengine.extension.comoncore.save.ISaveManager;
import com.benjastudio.andengine.extension.comoncore.scene.ISceneManager;

public interface IManagerBundle<TSaveManager extends ISaveManager<?, ?>, TFacebookManager extends IFacebookManager<?>, TLevelManager extends ILevelManager<?>, TStoryLevelManager extends ILevelManager<?>, TLevelForViewerManager extends ILevelManager<?>, TResourceManager extends IResourceManager<?, ?>, TSceneManager extends ISceneManager<?, ?>> {
	public abstract TSaveManager getSaveManager();

	public abstract TFacebookManager getFacebookManager();

	public abstract TLevelManager getLevelManager();

	public abstract TStoryLevelManager getStoryLevelManager();

	public abstract TLevelForViewerManager getLevelManagerForViewer();

	public abstract TResourceManager getResourceManager();

	public abstract TSceneManager getSceneManager();
}
