package com.benjastudio.andengine.extension.comoncore.entity.ui.button;

import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.text.AutoWrap;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.util.HorizontalAlign;

import android.util.Log;

import com.benjastudio.andengine.extension.comoncore.IManagerBundle;
import com.benjastudio.andengine.extension.comoncore.scene.IBaseScene;
import com.benjastudio.andengine.extension.comoncore.facebook.FacebookObserver;

public class FacebookLoginButton<T extends IManagerBundle<?, ?, ?, ?, ?, ?, ?>, TSceneType extends Enum<TSceneType>>
		extends RoundedButtonSprite<T, TSceneType> implements FacebookObserver {

	final Text logoutText;
	final Text waitText;
	final Text regularText;

	static final String TAG = "FacebookLoginButton";

	public FacebookLoginButton(float pX, float pY,
			final IBaseScene<T, TSceneType> pScene) {
		super(pX, pY,
				pScene.mBundle.getResourceManager().comon.iconFacebook_TR,
				pScene, false, true, null);
		pScene.addSceneObserver(FacebookLoginButton.this);
		OnClickListener clickListener = new OnClickListener() {
			@Override
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				Log.d(TAG, "FacebookLoginButton: click...");
				regularText.setVisible(false);
				waitText.setVisible(true);
				if (pScene.mBundle.getFacebookManager().isLoggedIn())
					pScene.mBundle.getFacebookManager().logout();
				else
					pScene.mBundle.getFacebookManager().login();
			}
		};
		setOnClickListener(clickListener);

		setColor(0.4f, 0.4f, 0.8f);
		pScene.mBundle.getFacebookManager().addObserver(
				FacebookLoginButton.this);

		logoutText = new Text(0, 0,
				pScene.mBundle.getResourceManager().comon.font, "Logout",
				pScene.eBundle.vbom);
		logoutText.setScale(0.7f);
		logoutText.setVisible(false);
		logoutText.setPosition(
				getWidth() * 0.5f - logoutText.getWidth() * 0.5f,
				getHeightScaled() * 0.5f + 24);
		attachChild(logoutText);
		if (scene.mBundle.getFacebookManager() != null
				&& scene.mBundle.getFacebookManager().isLoggedIn())
			logoutText.setVisible(true);

		waitText = new Text(0, 0,
				pScene.mBundle.getResourceManager().comon.font, "Wait...",
				pScene.eBundle.vbom);
		waitText.setScale(0.7f);
		waitText.setVisible(false);
		waitText.setPosition(getWidth() * 0.5f - waitText.getWidth() * 0.5f,
				getHeightScaled() * 0.5f + 24);
		attachChild(waitText);

		regularText = new Text(0, 0,
				pScene.mBundle.getResourceManager().comon.font, "Connect",
				new TextOptions(AutoWrap.WORDS, 64), pScene.eBundle.vbom);
		regularText.setHorizontalAlign(HorizontalAlign.CENTER);
		regularText.setScale(0.7f);
		regularText.setVisible(true);
		if (scene.mBundle.getFacebookManager() != null
				&& scene.mBundle.getFacebookManager().isLoggedIn())
			regularText.setVisible(false);
		regularText.setPosition(getWidth() * 0.5f - regularText.getWidth()
				* 0.5f, getHeightScaled() * 0.5f + 24);
		attachChild(regularText);
	}

	@Override
	public void onLoggedIn() {
		regularText.setVisible(false);
		waitText.setVisible(false);
		logoutText.setVisible(true);
		scene.mBundle.getFacebookManager().toast(
				"Welcome " + scene.mBundle.getFacebookManager().getFirstName());
		Log.d(TAG, "onLoggedIn...");
	}

	@Override
	public void onLoggedInFailed(String error) {
		regularText.setVisible(true);
		waitText.setVisible(false);
		scene.mBundle.getFacebookManager().toast(error);
		Log.d(TAG, "onLoggedInFailed...");
	}

	@Override
	public void onLoggedOut() {
		regularText.setVisible(true);
		waitText.setVisible(false);
		logoutText.setVisible(false);
		scene.mBundle.getFacebookManager().toast("Logged out.");
		Log.d(TAG, "onLoggedOut...");
	}

	@Override
	public void onSceneDispose(IBaseScene<?, ?> pScene) {
		super.onSceneDispose(pScene);
		Log.d(TAG, "onSceneDispose");
		pScene.mBundle.getFacebookManager().removeObserver(
				FacebookLoginButton.this);
	}

	@Override
	public void onGraphUserSet() {
		Log.d(TAG, "onGraphUserSet...");
	}
}