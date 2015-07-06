package com.benjastudio.andengine.extension.comoncore.facebook;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.facebook.HttpMethod;
import com.facebook.Response;

public abstract class FacebookRequestDataRunnable implements IOnSucessOnFail {

	static final String TAG = "FacebookRequestDataRunnable";

	public void onCompleted(Response pResponse) {

		if (pResponse != null && pResponse.getRawResponse() != null) {

			try {

				JSONObject obj = new JSONObject(pResponse.getRawResponse());

				if (obj != null && !obj.isNull("success")
						&& obj.get("success").equals(true)) {
					onSuccess(pResponse);
					return;
				} else if (obj != null
						&& pResponse.getRequest().getHttpMethod()
								.equals(HttpMethod.GET) && !obj.isNull("data")) {
					onSuccess(pResponse);
					return;
				} else if (obj != null
						&& pResponse.getRequest().getHttpMethod()
								.equals(HttpMethod.POST) && !obj.isNull("id")) {
					onSuccess(pResponse);
					return;
				}

			} catch (JSONException e) {
				e.printStackTrace();
				onFailWithLogError(pResponse);
				return;
			}
		}

		onFailWithLogError(pResponse);
	}

	private void onFailWithLogError(Response pResponse) {

		Log.d(TAG, "LogError: Error=" + pResponse.getError());

		if (pResponse.getError() != null) {
			Log.d(TAG, "LogError: ErrorMessage="
					+ pResponse.getError().getErrorMessage());
			Log.d(TAG, "LogError: ErrorUserMessage="
					+ pResponse.getError().getErrorUserMessage());
		}

		Log.d(TAG, "LogError: Error graphObject=" + pResponse.getGraphObject());

		if (pResponse.getGraphObject() != null) {
			Log.d(TAG, "LogError: graphObject InnerJSON="
					+ pResponse.getGraphObject().getInnerJSONObject());
		}

		Log.d(TAG, "LogError: Raw Reponse=" + pResponse.getRawResponse());

		onFail(pResponse);
	}
}