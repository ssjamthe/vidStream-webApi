package com.appify.vidstream.newWebApi.data;

import java.util.Map;

public interface PropertyDataLoader {
	void startLoading();

    void stopLoading();

    Map<String, String> getProps();

}
