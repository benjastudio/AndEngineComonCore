package com.benjastudio.andengine.extension.comoncore.facebook;

import com.facebook.Response;

public interface IOnSucessOnFail {
	abstract public void onSuccess(Response pResponse);

	abstract public void onFail(Response pResponse);
}
