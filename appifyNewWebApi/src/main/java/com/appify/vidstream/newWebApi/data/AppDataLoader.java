package com.appify.vidstream.newWebApi.data;

import java.util.Map;

import com.appify.vidstream.newWebApi.data.AppInfo;

public interface AppDataLoader {

	 Map<String, AppInfo> getAppsData();

	 public abstract void startLoading();

	 public abstract void stopLoading();
}
