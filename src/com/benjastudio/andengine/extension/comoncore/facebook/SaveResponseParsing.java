package com.benjastudio.andengine.extension.comoncore.facebook;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SaveResponseParsing {

	static final String TAG_DESCRIPTION = "description";

	static public final Map<String, String> getIdDescriptionMapFromDataJSONArray(
			JSONArray jsonArray) {

		if (jsonArray == null)
			return null;

		try {

			Map<String, String> idDescriptionMap = new HashMap<String, String>();
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject data = jsonArray.getJSONObject(i);
				String description = ResponseParsing.unescape(data
						.getString(TAG_DESCRIPTION));
				idDescriptionMap.put(data.getString(ResponseParsing.TAG_ID),
						description);
			}
			return idDescriptionMap;

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}
}
