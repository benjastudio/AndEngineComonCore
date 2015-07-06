package com.benjastudio.andengine.extension.comoncore;

import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.SimpleAsyncGameActivity;
import org.andengine.util.call.Callback;
import org.andengine.util.debug.Debug;
import org.andengine.util.exception.CancelledException;
import org.andengine.util.progress.IProgressListener;
import org.andengine.util.progress.ProgressCallable;

import com.benjastudio.andengine.extension.comoncore.scene.IBaseScene;

import android.os.AsyncTask;

public abstract class CustomSimpleAsyncGameActivity<T extends IManagerBundle<?, ?, ?, ?, ?, ?, ?>, TSceneType extends Enum<TSceneType>

> extends SimpleAsyncGameActivity {

	public abstract IBaseScene<T, TSceneType> showLoadScene();

	public abstract void onCreateBasicsResources();

	public abstract Scene _onCreateScene();

	@Override
	public void onCreateResources(
			final OnCreateResourcesCallback pOnCreateResourcesCallback) {

		onCreateBasicsResources();
		final IBaseScene<T, TSceneType> loadingScene = showLoadScene();

		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {

				final ProgressCallable<Void> pCallable = new ProgressCallable<Void>() {
					@Override
					public Void call(final IProgressListener pProgressListener)
							throws Exception {
						CustomSimpleAsyncGameActivity.this
								.onCreateResourcesAsync(pProgressListener);
						pProgressListener.onProgressChanged(100);
						pOnCreateResourcesCallback.onCreateResourcesFinished();
						return null;
					}
				};

				final Callback<Void> pCallback = new Callback<Void>() {
					@Override
					public void onCallback(final Void pCallbackValue) {
					}
				};

				new AsyncTask<Void, Integer, Void>() {
					private Exception mException = null;

					@Override
					public void onPreExecute() {
						super.onPreExecute();
					}

					@Override
					public Void doInBackground(final Void... params) {
						try {
							return pCallable.call(new IProgressListener() {
								@Override
								public void onProgressChanged(
										final int pProgress) {
									onProgressUpdate(pProgress);
								}
							});
						} catch (final Exception e) {
							this.mException = e;
						}
						return null;
					}

					@Override
					public void onProgressUpdate(final Integer... values) {
						loadingScene.setProgress(values[0]);
					}

					@Override
					public void onPostExecute(final Void result) {
						if (this.isCancelled()) {
							this.mException = new CancelledException();
						}

						if (this.mException == null) {
							pCallback.onCallback(result);
						} else {
							Debug.e("Error", this.mException);
						}

						super.onPostExecute(result);
					}
				}.execute((Void[]) null);
			}
		});
	}

	@Override
	public void onCreateScene(final OnCreateSceneCallback pOnCreateSceneCallback) {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Scene scene;
				try {
					scene = CustomSimpleAsyncGameActivity.this._onCreateScene();
					pOnCreateSceneCallback.onCreateSceneFinished(scene);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public Scene onCreateSceneAsync(IProgressListener pProgressListener)
			throws Exception {
		return null;
	}

	@Override
	public void onPopulateScene(final Scene pScene,
			final OnPopulateSceneCallback pOnPopulateSceneCallback) {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				pOnPopulateSceneCallback.onPopulateSceneFinished();
			}
		});
	}

	@Override
	public void onPopulateSceneAsync(Scene pScene,
			IProgressListener pProgressListener) throws Exception {
	}
}
