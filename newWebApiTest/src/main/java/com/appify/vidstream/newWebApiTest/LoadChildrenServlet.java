package com.appify.vidstream.newWebApiTest;

import com.appify.vidstream.newWebApiTest.LoadAppResponse;
import com.appify.vidstream.newWebApiTest.data.AppDataLoader;
import com.appify.vidstream.newWebApiTest.data.AppInfo;
import com.google.inject.Provider;
import com.google.inject.servlet.RequestParameters;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Created by swapnil on 30/11/16.
 */
public class LoadChildrenServlet extends HttpServlet{

    private AppDataLoader appDataLoader;
    private Provider<Map<String, String[]>> paramsProvider;

    @Inject
    public  LoadChildrenServlet(AppDataLoader appDataLoader,
                                @RequestParameters Provider<Map<String, String[]>> paramsProvider){
        this.paramsProvider = paramsProvider;
        this.appDataLoader = appDataLoader;
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Map<String, AppInfo> appsInfoMap = appDataLoader.getAppsData();

        Map<String, String[]> params = paramsProvider.get();

        String deviceId = params.get("deviceId")[0];

        String appId = params.get("appId")[1];

        String tabId = params.get("tabId")[2];

        String categorizationId = params.get("categorizationId")[3];

        String categoryId = params.get("categoryId")[4];

        String orderAttribute = params.get("orderAttribute")[5];

        String pageNo = params.get("pageNo")[6];



        AppInfo appInfo = appsInfoMap.get(appId);

        int videosPerCall = appInfo.getVideosPerCall();

        LoadAppResponse response = new LoadAppResponse();

    }
}
