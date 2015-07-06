package com.benjastudio.andengine.extension.comoncore.facebook;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ResponseParsing {

	static final String TAG_DATA = "data";
	static final String TAG_ID = "id";
	static final String TAG_USER = "user";
	static final String TAG_NAME = "name";

	static public String escape(String str) {
		String newStr = str.replace('"', '#');
		return newStr;
	}

	static public String unescape(String str) {
		String newStr = str.replace('#', '"');
		return newStr;
	}

	static public final JSONArray getDataJSONArrayFromRawResponse(
			String rawResponse) {

		if (rawResponse == null)
			return null;

		try {

			JSONObject jsonObj = new JSONObject(rawResponse);
			return jsonObj.getJSONArray(TAG_DATA);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}
}