package com.appify.vidstream.newWebApiTest;

import com.appify.vidstream.newWebApiTest.data.*;
import com.google.inject.Provider;
import com.google.inject.servlet.RequestParameters;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by swapnil on 28/11/16.
 */
@Singleton
public class LoadAppServlet extends HttpServlet {

    private AppDataLoader appDataLoader;
    private List<Tab> tabs;
    private List<TabDataLoader> tabDataLoaderList;
    private Provider<Map<String, String[]>> paramsProvider;


    @Inject
    public LoadAppServlet(AppDataLoader appDataLoader,
                          @RequestParameters Provider<Map<String, String[]>> paramsProvider,
                          @Annotations.TabDataLoaders List<TabDataLoader> tabDataLoaderList) {
        this.appDataLoader = appDataLoader;
        this.paramsProvider = paramsProvider;
    }


    @Override
    public void doGet(
            HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Map<String, AppInfo> appsInfoMap = appDataLoader.getAppsData();

        Map<String, String[]> params = paramsProvider.get();

        String appId = params.get("appId")[0];

        AppInfo appInfo = appsInfoMap.get(appId);

        LoadAppResponse response = new LoadAppResponse();

        // Needs to add HomeTabDataLoader ,  NewlyAdded TabDataLoader in Tabs json object
    }


}
