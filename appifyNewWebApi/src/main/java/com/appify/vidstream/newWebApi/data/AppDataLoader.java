package com.appify.vidstream.newWebApi.data;

import java.util.Map;

import com.appify.vidstream.newWebApi.data.AppInfo;
import com.appify.vidstream.newWebApi.util.AbstractScheduledServiceRobust;
import com.google.common.util.concurrent.AbstractScheduledService;

public abstract class AppDataLoader extends AbstractScheduledServiceRobust {

    public abstract Map<String, AppInfo> getAppsData();
}
