package com.benjastudio.andengine.extension.comoncore.save;

import java.util.HashMap;
import java.util.Map;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;

import com.benjastudio.andengine.extension.comoncore.ConstantVar;
import com.benjastudio.andengine.extension.comoncore.IManagerBundle;
import com.benjastudio.andengine.extension.comoncore.facebook.FacebookRequestDataRunnable;
import com.benjastudio.andengine.extension.comoncore.facebook.ScoreResponseParsing;
import com.benjastudio.andengine.extension.comoncore.level.ILevel;
import com.facebook.Response;
import com.facebook.Session;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

public abstract class ISaveManager<T extends ISaveGame<T>, TManagerBundle extends IManagerBundle<?, ?, ?, ?, ?, ?, ?>> {

	static final protected String DEFAULT_SAVE_NAME = "default";
	static final private String TAG = "ISaveManager";

	protected TManagerBundle mBundle;
	protected Activity activity;
	protected Engine engine;

	protected SharedPreferences gameProgression;
	protected Map<String, Integer> friendProgressions;
	protected int totalStars = -1;

	private boolean isPrepared;

	public ISaveManager() {
		isPrepared = false;
	}

	public void prepareSaveManager(Activity pActivity, Engine pEngine,
			TManagerBundle pMBundle) {
		activity = pActivity;
		engine = pEngine;
		mBundle = pMBundle;
		gameProgression = activity.getSharedPreferences(
				ConstantVar.GAMEPROGRESSION_DB_NAME, Context.MODE_PRIVATE);
		resetSaveGame();
		friendProgressions = new HashMap<String, Integer>();
		isPrepared = true;
	}

	public abstract String currentSaveGameToString();

	public abstract void mergeSaveGame(T pSaveGame);

	public abstract T createNewSaveGame();

	public abstract Map<String, T> getIdSaveGameMapFromRawResponse(
			String rawResponse);

	public abstract float getBestScoreOf(int world, int level);

	public abstract int getNumberOfStarsForScore(float score);

	public abstract void saveScore(int world, int level, float score);

	public abstract void resetSaveGame();

	public abstract T getCurrentSaveGame();

	public abstract void refreshTotalStars();

	public abstract boolean isUnlocked(int world, int level);

	public void synchronizeAll(ILevel lastLevel) {
		synchronize();
		saveProgression(lastLevel);
		synchronizeFriendProgressions();
	}

	public void synchronize() {
		T localSave = createNewSaveGame();
		// Merge local save and current save
		localSave.loadFromSharedPreferences(gameProgression, DEFAULT_SAVE_NAME);
		mergeSaveGame(localSave);
		synchronizeNewTry(0);
	}

	public void synchronizeNewTry(final int tryCounter) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (mBundle.getFacebookManager() != null
						&& mBundle.getFacebookManager().isLoggedIn()) {

					// Fetch remote saves
					mBundle.getFacebookManager().fetchData(
							mBundle.getFacebookManager().getGraphPathSave(),
							new FacebookRequestDataRunnable() {

								@Override
								public void onSuccess(Response pResponse) {
									Log.d(TAG, "fetchData success");

									Map<String, T> map = getIdSaveGameMapFromRawResponse(pResponse
											.getRawResponse());

									if (map != null && !map.isEmpty()) {
										Log.d(TAG, "saveGame will be updated");

										// Merge all remote save with current
										String saveGameIdToUpdate = "";
										for (Map.Entry<String, T> entry : map
												.entrySet()) {
											mergeSaveGame(entry.getValue());
											saveGameIdToUpdate = entry.getKey(); // arbitrary
																					// selection
										}

										// Delete all (useless) remote saves
										// except one, saveGameIdToUpdate (in
										// order to keep only 1 saveGame)
										for (Map.Entry<String, T> entry : map
												.entrySet()) {
											if (entry.getKey() != saveGameIdToUpdate)
												deleteFacebookSave(entry
														.getKey());
										}

										// Update remote save
										Bundle params = mBundle
												.getFacebookManager()
												.createEscapedParamsObject(
														DEFAULT_SAVE_NAME,
														currentSaveGameToString());
										Log.d(TAG, "update params object="
												+ params.getString("object"));

										mBundle.getFacebookManager()
												.updateData(
														saveGameIdToUpdate,
														params,
														new FacebookRequestDataRunnable() {
															@Override
															public void onSuccess(
																	Response pResponse) {
																Log.d(TAG,
																		"updateData success");
																saveCheck(true,
																		tryCounter);
															}

															@Override
															public void onFail(
																	Response pResponse) {
																Log.w(TAG,
																		"updateData fail");
																saveCheck(
																		false,
																		tryCounter);
															}
														});

									} else {
										// First remote save
										Log.d(TAG, "saveGame will be created");
										Bundle params = mBundle
												.getFacebookManager()
												.createEscapedParamsObject(
														DEFAULT_SAVE_NAME,
														currentSaveGameToString());
										Log.d(TAG, "new params object="
												+ params.getString("object"));

										mBundle.getFacebookManager()
												.postData(
														mBundle.getFacebookManager()
																.getGraphPathSave(),
														params,
														new FacebookRequestDataRunnable() {
															@Override
															public void onSuccess(
																	Response pResponse) {
																Log.d(TAG,
																		"postData success");
																saveCheck(true,
																		tryCounter);
															}

															@Override
															public void onFail(
																	Response pResponse) {
																Log.w(TAG,
																		"postData fail");
																saveCheck(
																		false,
																		tryCounter);
															}
														});
									}
								}

								@Override
								public void onFail(Response pResponse) {
									Log.w(TAG, "fetchData fail");
									saveCheck(false, tryCounter);
								}
							});

				} else {
					saveToSharedPreferences(DEFAULT_SAVE_NAME);
				}
			}
		});
	}

	private void saveCheck(boolean success, int tryCounter) {
		tryCounter++;
		if (!success && tryCounter < 3) {
			synchronizeNewTry(tryCounter);
		} else {
			if (success)
				Log.d(TAG, "save with success.");
			else
				Log.w(TAG, "(remote) save failed.");
			saveToSharedPreferences(DEFAULT_SAVE_NAME);
		}
	}

	private void saveToSharedPreferences(final String saveName) {
		SharedPreferences.Editor spe = gameProgression.edit();
		spe.putString(saveName, currentSaveGameToString());
		spe.commit();
	}

	private void deleteSharedPreferencesSave(final String saveName) {
		SharedPreferences.Editor spe = gameProgression.edit();
		spe.clear();
		spe.commit();
	}

	public boolean isPrepared() {
		return isPrepared;
	}

	protected void deleteFacebookSave(String id) {
		mBundle.getFacebookManager().deleteData(id,
				new FacebookRequestDataRunnable() {
					@Override
					public void onSuccess(Response pResponse) {
						Log.d(TAG, "deleteData success");
					}

					@Override
					public void onFail(Response pResponse) {
						Log.w(TAG, "deleteData fail");
					}
				});
	}

	public void deleteAllSaves() {
		deleteFacebookSave();
		deleteSharedPreferencesSave(DEFAULT_SAVE_NAME);
		resetSaveGame();
	}

	private void deleteFacebookSave() {
		mBundle.getFacebookManager().fetchData(
				mBundle.getFacebookManager().getGraphPathSave(),
				new FacebookRequestDataRunnable() {
					@Override
					public void onSuccess(Response pResponse) {
						Map<String, T> map = getIdSaveGameMapFromRawResponse(pResponse
								.getRawResponse());
						if (map != null && !map.isEmpty()) {
							for (Map.Entry<String, T> entry : map.entrySet()) {
								Log.d(TAG, "delete save id=" + entry.getKey());
								deleteFacebookSave(entry.getKey());
							}
						}
					}

					@Override
					public void onFail(Response pResponse) {
						Log.w(TAG, "fetchData fail");
					}
				});
	}

	public final Map<String, T> getIdSaveGameMapFromIdDescriptionMap(
			Map<String, String> idDescriptionMap) {

		if (idDescriptionMap == null)
			return null;

		Map<String, T> idSaveGameMap = new HashMap<String, T>();

		for (Map.Entry<String, String> entry : idDescriptionMap.entrySet()) {
			T saveGame = createNewSaveGame();
			saveGame.loadFromJson(entry.getValue());
			idSaveGameMap.put(entry.getKey(), saveGame);
		}

		return idSaveGameMap;
	}

	private int toProgression(int world, int level) {
		// Facebook Scores API can only store integer values, so:
		// world W, level L, format: WWWLLL
		return world * 1000 + level;
	}

	private int toWorld(int progression) {
		return progression / 1000;
	}

	private int toLevel(int progression) {
		return progression % 1000;
	}

	public int getTotalStars() {
		if (totalStars == -1)
			refreshTotalStars();
		return totalStars;
	}

	public int getNumberOfStars(int world, int level) {
		return getNumberOfStarsForScore(getBestScoreOf(world, level));
	}

	public boolean isUnlocked(ILevel level) {
		return isUnlocked(level.getWorld(), level.getLevel());
	}

	public void saveProgression(ILevel level) {
		saveProgression(level.getWorld(), level.getLevel());
	}

	public void saveProgression(int world, int level) {
		saveProgression(toProgression(world, level));
	}

	private void saveProgression(final int progression) {
		refreshTotalStars();
		Log.d(TAG, "Save high score: " + progression);
		if (mBundle.getFacebookManager().isLoggedIn()) {
			Bundle params = new Bundle();
			params.putInt("score", progression);
			mBundle.getFacebookManager().postData("me/scores", params,
					new FacebookRequestDataRunnable() {
						@Override
						public void onSuccess(Response pResponse) {
							Log.d(TAG, "High score saved.");
							synchronizeFriendProgressions();
						}

						@Override
						public void onFail(Response pResponse) {
							Log.w(TAG, "High score save failed.");
							// Modify change locally
							friendProgressions.put(mBundle.getFacebookManager()
									.getName(), progression);
						}
					});
		} else {
			Log.w(TAG, "Not logged in. Can't save high score.");
		}
	}

	public Map<String, Integer> getFriendProgressions(int world) {
		HashMap<String, Integer> _friendProgressions = new HashMap<String, Integer>();
		for (Map.Entry<String, Integer> entry : friendProgressions.entrySet()) {
			int progression = entry.getValue();
			if (toWorld(progression) == world) {
				_friendProgressions.put(entry.getKey(), toLevel(progression));
			}
		}
		return _friendProgressions;
	}

	public Map<String, Integer> getFriendProgressionsBefore(int world) {
		HashMap<String, Integer> _friendProgressions = new HashMap<String, Integer>();
		for (Map.Entry<String, Integer> entry : friendProgressions.entrySet()) {
			int progression = entry.getValue();
			if (toWorld(progression) < world) {
				_friendProgressions.put(entry.getKey(), toLevel(progression));
			}
		}
		return _friendProgressions;
	}

	public Map<String, Integer> getFriendProgressionsAfter(int world) {
		HashMap<String, Integer> _friendProgressions = new HashMap<String, Integer>();
		for (Map.Entry<String, Integer> entry : friendProgressions.entrySet()) {
			int progression = entry.getValue();
			if (toWorld(progression) > world) {
				_friendProgressions.put(entry.getKey(), toLevel(progression));
			}
		}
		return _friendProgressions;
	}

	public void synchronizeFriendProgressions() {
		synchronizeFriendProgressions(0);
	}

	private void synchronizeFriendProgressions(final int retry) {
		if (retry < 2) {
			if (retry == 0) {
				synchronizeFriendProgressionsTry(retry + 1);
			} else {
				if (engine != null) {
					engine.registerUpdateHandler(new TimerHandler(1f, false,
							new ITimerCallback() {
								public void onTimePassed(
										TimerHandler pTimerHandler) {
									synchronizeFriendProgressionsTry(retry + 1);
								};
							}));
				}
			}
		} else {
			Log.w(TAG, "Synchronize Friend Hight Scores not possible after "
					+ retry + " retries.");
		}
	}

	private void synchronizeFriendProgressionsTry(final int retry) {
		Log.d(TAG, "Synchronize Friend Hight Scores.");

		final String appId = getAppId();

		if (mBundle.getFacebookManager().isLoggedIn() && appId != null) {
			mBundle.getFacebookManager().fetchData(appId + "/scores",
					new FacebookRequestDataRunnable() {
						@Override
						public void onSuccess(Response pResponse) {
							Log.d(TAG, "Friend Hight Scores fetched.");
							Map<String, Integer> nameProgressionMap = ScoreResponseParsing
									.getNameProgressionMapFromRawResponse(pResponse
											.getRawResponse());
							if (nameProgressionMap != null) {
								friendProgressions.putAll(nameProgressionMap);
							} else {
								Log.e(TAG, "Bad parsing..");
							}
						}

						@Override
						public void onFail(Response pResponse) {
							Log.w(TAG, "Can't fetch Friend Hight Scores.");
							synchronizeFriendProgressions(retry);
						}
					});
		} else {
			Log.w(TAG,
					"Not logged in and/or appId unreachable. Can't fetch high score.");
			synchronizeFriendProgressions(retry);
		}
	}

	public String getAppId() {
		if (Session.getActiveSession() == null) {
			Log.w(TAG, "Can't get app id. No active session.");
			return null;
		}
		return Session.getActiveSession().getApplicationId();
	}

	public void showRemoteSaveLog() {
		mBundle.getFacebookManager().fetchData(
				mBundle.getFacebookManager().getGraphPathSave(),
				new FacebookRequestDataRunnable() {
					@Override
					public void onSuccess(Response pResponse) {
						Log.d(TAG, "fetchData success");
						Log.d(TAG, pResponse.getRawResponse());

						Map<String, T> map = getIdSaveGameMapFromRawResponse(pResponse
								.getRawResponse());

						for (Map.Entry<String, T> entry : map.entrySet()) {
							Log.d(TAG, entry.getKey() + " :");
							Log.d(TAG, entry.getValue().toString());
						}
					}

					@Override
					public void onFail(Response pResponse) {
						Log.w(TAG, "fetchData fail");
					}
				});
	}
}
