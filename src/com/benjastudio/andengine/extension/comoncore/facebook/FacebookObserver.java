package com.benjastudio.andengine.extension.comoncore.facebook;

public interface FacebookObserver {
	public void onLoggedIn();

	public void onLoggedInFailed(String error);

	public void onLoggedOut();

	public void onGraphUserSet();
}
