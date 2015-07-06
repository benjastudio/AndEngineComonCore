package com.benjastudio.andengine.extension.comoncore.facebook;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ScoreResponseParsing {

	static final String TAG_SCORE = "score";

	static public final Map<String, Integer> getNameProgressionMapFromDataJSONArray(
			JSONArray jsonArray) {

		if (jsonArray == null)
			return null;

		try {

			Map<String, Integer> nameProgressionMap = new HashMap<String, Integer>();
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject data = jsonArray.getJSONObject(i);
				final long progression = data.getLong(TAG_SCORE);
				JSONObject user = data.getJSONObject(ResponseParsing.TAG_USER);
				final String name = user.getString(ResponseParsing.TAG_NAME);
				nameProgressionMap.put(name, (int) progression);
			}
			return nameProgressionMap;

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	static public final Map<String, Integer> getNameProgressionMapFromRawResponse(
			String rawResponse) {
		return getNameProgressionMapFromDataJSONArray(ResponseParsing
				.getDataJSONArrayFromRawResponse(rawResponse));
	}
}