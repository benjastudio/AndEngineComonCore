package com.benjastudio.andengine.extension.comoncore.save;

import android.content.SharedPreferences;
import android.util.Log;

public abstract class ISaveGame<T> {

	private static final String TAG = "ISaveGame";

	public abstract boolean loadFromJson(String json);

	public abstract T unionWith(T other);

	public abstract void merge(T other);

	public abstract String toString();

	public abstract T clone();

	public boolean loadFromData(byte[] data) {
		if (data == null)
			return false;
		return loadFromJson(new String(data));
	}

	public byte[] toBytes() {
		return toString().getBytes();
	}

	public boolean loadFromSharedPreferences(SharedPreferences sP,
			String saveName) {
		if (sP == null || saveName == null) {
			Log.e(TAG, "SharedPreferences or save name is null");
			return false;
		} else {
			return loadFromJson(sP.getString(saveName, null));
		}
	}
}
