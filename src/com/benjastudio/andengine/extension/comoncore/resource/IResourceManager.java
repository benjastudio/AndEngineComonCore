package com.benjastudio.andengine.extension.comoncore.resource;

import java.util.List;

import org.andengine.util.progress.IProgressListener;

import android.util.Log;

import com.benjastudio.andengine.extension.comoncore.EngineBundle;
import com.benjastudio.andengine.extension.comoncore.IManagerBundle;

public abstract class IResourceManager<T extends IManagerBundle<?, ?, ?, ?, ?, ?, ?>, TDynamicResourceLoader extends ResourceLoader> {

	final static private String TAG = "IResourceManager";

	public ComonResourceLoader comon;

	protected EngineBundle eBundle;
	protected T mBundle;
	public List<TDynamicResourceLoader> worldList;

	protected abstract void initWorldList();

	public void prepareManager(EngineBundle pEBundle, T pMbundle) {
		eBundle = pEBundle;
		mBundle = pMbundle;

		comon = new ComonResourceLoader(eBundle.activity, eBundle.engine,
				eBundle.musicManager, "Comon");

		initWorldList();
	}

	public void loadResourcesDynamic(int world) {
		MultipleProgressListener multipleProgressListener = new MultipleProgressListener(
				new IProgressListener() {
					@Override
					public void onProgressChanged(int pProgress) {
					}
				});
		loadResourcesDynamic(world, multipleProgressListener);
	}

	public void loadResourcesDynamic(final int world,
			MultipleProgressListener pMultipleProgressListener) {
		Log.v(TAG, "loadResourcesDynamic(" + world + ")");
		for (int i = 1; i < worldList.size(); i++) {
			if ((worldList.get(i).isLoaded()) && (world != i)) {
				worldList.get(i).unload();
			}
		}
		if (!worldList.get(world).isLoaded()) {
			worldList.get(world).load(pMultipleProgressListener);
		}
	}

	public void refreshResourcesDynamic() {
		Log.v(TAG, "refreshResourcesDynamic()");
		for (int i = 1; i < worldList.size(); i++) {
			if (worldList.get(i).isLoaded()) {
				worldList.get(i).refresh();
			}
		}
	}
}
