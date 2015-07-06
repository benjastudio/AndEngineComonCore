package com.benjastudio.andengine.extension.comoncore.facebook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.andengine.engine.Engine;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.benjastudio.andengine.extension.comoncore.save.ISaveGame;
import com.benjastudio.andengine.extension.comoncore.save.ISaveManager;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionDefaultAudience;
import com.facebook.SessionState;
import com.facebook.Session.StatusCallback;
import com.facebook.model.GraphUser;

public abstract class IFacebookManager<T extends ISaveGame<T>> {

	Activity activity;
	Engine engine;
	ISaveManager<T, ?> saveManager;

	static final protected String GRAPH_PATH_SCORE = "me/scores";
	static final protected String OBJECT_PARAM = "object";

	static final List<String> PERMISSIONS_READ = Arrays.asList(
			"public_profile", "user_friends");
	static final List<String> PERMISSIONS_PUBLISH = Arrays
			.asList("publish_actions");
	int publishPermissionTryCounter;

	static final String TAG = "FacebookManager";

	List<FacebookObserver> observers;

	public GraphUser currentUser;

	boolean isLoggedIn;
	boolean loginInWaitingState;
	boolean autoLoginInWaitingState;
	boolean sessionClosingState;

	public void prepareManager(Activity pActivity, Engine pEngine,
			ISaveManager<T, ?> pISaveManager) {
		activity = pActivity;
		engine = pEngine;
		saveManager = pISaveManager;

		observers = new ArrayList<FacebookObserver>();
		isLoggedIn = false;
		loginInWaitingState = false;
		autoLoginInWaitingState = false;
		sessionClosingState = false;
		publishPermissionTryCounter = 0;
		autoLogin();
	}

	/**
	 * should looks like: return "me/objects/pudipuda:savegame"
	 */
	public abstract String getGraphPathSave();

	public void addObserver(FacebookObserver observer) {
		Log.d(TAG, "addObserver");
		observers.add(observer);
	}

	public void removeObserver(FacebookObserver observer) {
		Log.d(TAG, "removeObserver");
		observers.remove(observer);
	}

	private void onLoggedIn(final Session session, SessionState state) {
		Log.d(TAG, "Logged in...");
		isLoggedIn = true;

		// Set GraphUser
		if (session.isOpened()) {
			Request.newMeRequest(session, new Request.GraphUserCallback() {
				@Override
				public void onCompleted(GraphUser user, Response response) {
					if (user != null) {

						currentUser = user;
						onGraphUserSet();

						// Notify
						engine.runOnUpdateThread(new Runnable() {
							@Override
							public void run() {
								for (FacebookObserver observer : observers)
									observer.onLoggedIn();
							}
						});

						if (saveManager != null && saveManager.isPrepared()) {
							saveManager.synchronize();
							saveManager.synchronizeFriendProgressions();
							// synchronize all instead?
						}

					} else
						logout(); // TODO: improve this case.
				}
			}).executeAsync();
		}
	}

	private void onLoggedInFailed() {
		Log.w(TAG, "Logged in failed...");
		// Notify
		final String error = "Please check your network connection.";
		engine.runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
				for (FacebookObserver observer : observers)
					observer.onLoggedInFailed(error);
			}
		});
	}

	private void onLoggedOut(Session session, SessionState state) {
		isLoggedIn = false;
		currentUser = null;
		publishPermissionTryCounter = 0;
		Log.d(TAG, "Logged out...");
		// Notify
		if (!sessionClosingState) {
			engine.runOnUpdateThread(new Runnable() {
				@Override
				public void run() {
					for (FacebookObserver observer : observers)
						observer.onLoggedOut();
				}
			});
			closeSession(false);
		}
	}

	private void onGraphUserSet() {
		Log.d(TAG, "onGraphUserSet...");
		// Notify
		if (observers.size() > 0) {
			// Sometimes this can be executed before than activity.engine is
			// created, it causes crash.
			// The line above (if(observers.size() > 0)) is a small security to
			// prevent it.
			engine.runOnUpdateThread(new Runnable() {
				@Override
				public void run() {
					for (FacebookObserver observer : observers)
						observer.onGraphUserSet();
				}
			});
		}
	}

	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		if (state.isOpened()) {
			loginInWaitingState = false;
			autoLoginInWaitingState = false;
			onLoggedIn(session, state);
		} else if (state.isClosed()) {
			if (loginInWaitingState) {
				loginInWaitingState = false;
				onLoggedInFailed();
			} else if (autoLoginInWaitingState) {
				autoLoginInWaitingState = false;
				Log.w(TAG, "autoLogin doesn't success.");
			} else
				onLoggedOut(session, state);
		}
	}

	public void login() {
		Log.d(TAG, "login asked.");
		loginInWaitingState = true;
		Session.openActiveSession(activity, true, PERMISSIONS_READ,
				new StatusCallback() {
					@Override
					public void call(Session session, SessionState state,
							Exception exception) {
						onSessionStateChange(session, state, exception);
					}
				});
	}

	public void autoLogin() {
		Log.d(TAG, "autologin asked.");
		autoLoginInWaitingState = true;
		Session.openActiveSession(activity, false, PERMISSIONS_READ,
				new StatusCallback() {
					@Override
					public void call(Session session, SessionState state,
							Exception exception) {
						onSessionStateChange(session, state, exception);
					}
				});
	}

	public void logout() {
		Log.d(TAG, "logout asked.");
		Session activeSession = Session.getActiveSession();
		if (activeSession != null) {
			activeSession.closeAndClearTokenInformation();
		}
	}

	private boolean hasPublishPermissions(Session session) {
		List<String> permissions = session.getPermissions();
		return permissions.containsAll(PERMISSIONS_PUBLISH);
	}

	private void addPublishPermissions(final Session session) {

		if (!hasPublishPermissions(session) && publishPermissionTryCounter < 1) {

			// Prompt an alert dialog (yes/no)
			new AlertDialog.Builder(activity)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int id) {

									// Request
									Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(
											activity, PERMISSIONS_PUBLISH)
											.setDefaultAudience(
													SessionDefaultAudience.FRIENDS)
											.setRequestCode(-1);
									session.requestNewPublishPermissions(newPermissionsRequest);

								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

									// Logout
									// This permission is needed to see your
									// friends progression. Nothing will be
									// published.
									toast("Cette permission est necessaire pour voir la progression de vos amis dans le jeu. Rien ne sera posté sur votre fil d'actualité.");
									logout();

								}
							})
					.setTitle("Save Game Progression and Score")
					.setMessage(
							"Facebook let us to save your progression online. You'll be able to recover your progression cross your all devices and see the progression of your friends in the game. Nothing will be posted on your timeline. Do you want to use it?")
					.show();

			publishPermissionTryCounter++;
		}
	}

	public void toast(final String text) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(activity.getApplicationContext(), text,
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	public void fetchData(final String graphPath,
			final FacebookRequestDataRunnable runnable) {
		requestData(HttpMethod.GET, graphPath, null, runnable);
	}

	public void postData(final String graphPath, final Bundle params,
			final FacebookRequestDataRunnable runnable) {
		requestData(HttpMethod.POST, graphPath, params, runnable);
	}

	public void updateData(final String objectId, final Bundle params,
			final FacebookRequestDataRunnable runnable) {
		requestData(HttpMethod.POST, "/" + objectId, params, runnable);
	}

	public void deleteData(final String objectId,
			final FacebookRequestDataRunnable runnable) {
		requestData(HttpMethod.DELETE, "/" + objectId, null, runnable);
	}

	public void requestData(final HttpMethod httpMethod,
			final String graphPath, final Bundle params,
			final FacebookRequestDataRunnable runnable) {
		Log.d(TAG, "requestData " + httpMethod.toString());
		if (isLoggedIn()) {
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Session session = Session.getActiveSession();

					if (httpMethod.equals(HttpMethod.POST)
							&& !hasPublishPermissions(session)) {

						Log.d(TAG, "Need publish permissions");
						addPublishPermissions(session);

					} else if (session.isOpened()) {

						Request request = new Request(session, graphPath,
								params, httpMethod, new Request.Callback() {
									public void onCompleted(Response response) {
										Log.d(TAG,
												"requestData "
														+ httpMethod.toString()
														+ " onCompleted");
										runnable.onCompleted(response);
									}
								});
						request.executeAsync();

					} else {
						Log.w(TAG,
								"Unnable to requestData "
										+ httpMethod.toString()
										+ ", currentSession is closed (?)");
					}
				}
			});
		}
	}

	public boolean isLoggedIn() {
		return isLoggedIn;
	}

	public void closeSession(boolean closeOnDestroy) {
		sessionClosingState = true;
		Session session = Session.getActiveSession();
		if (session != null)
			session.close();
		if (!closeOnDestroy)
			sessionClosingState = false;
	}

	public String getFirstName() {
		if (currentUser == null)
			return null;
		return currentUser.getFirstName();
	}

	public String getLastName() {
		if (currentUser == null)
			return null;
		return currentUser.getLastName();
	}

	public String getName() {
		if (currentUser == null)
			return null;
		return currentUser.getName();
	}

	public Bundle createEscapedParamsObject(String title, String description) {
		Bundle params = new Bundle();
		params.putString(
				OBJECT_PARAM,
				"{\"title\":\"" + ResponseParsing.escape(title)
						+ "\",\"description\":\""
						+ ResponseParsing.escape(description) + "\"}");
		return params;
	}
}
