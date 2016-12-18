package com.appify.vidstream.newWebApiTest;

import com.appify.vidstream.newWebApiTest.data.*;
import com.appify.vidstream.newWebApiTest.util.WebAPIUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        this.tabDataLoaderList = tabDataLoaderList;
    }


    @Override
    public void doGet(
            HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        System.out.print("Inside DoGet Of LoadAppServlet");
        Map<String, AppInfo> appsInfoMap = appDataLoader.getAppsData();

        Map<String, String[]> params = paramsProvider.get();

        String appId = params.get("appId")[0];

        AppInfo appInfo = appsInfoMap.get(appId);

        LoadAppResponse response = new LoadAppResponse();

        String jsonResponse = "";

        if (!tabDataLoaderList.isEmpty()) {
            Tab tab = new Tab();
            TabResp tabResp = new TabResp();
            TabToTabRespConverter tabToTabRespConverter = new TabToTabRespConverter();
            TabResp[] tabResps = new TabResp[tabDataLoaderList.size()];

            for (int i = 0; i < tabDataLoaderList.size(); i++) {
                tab = tabDataLoaderList.get(i).getTab(appId);
                tabResp = tabToTabRespConverter.getTabRespFromTabWithoutChild(tab);
                tabResps[i] = tabResp;
            }

            TabDataLoader selectedTabLoader = tabDataLoaderList.get(0);

            response.setSelectedTab(tabToTabRespConverter.getTabRespFromTab(selectedTabLoader.getTab(appId)));
            response.setTabs(tabResps);
            response.setShowAdMovingInside(appInfo.isShowAdMovingInside());
            response.setShowBanner(appInfo.isShowBanner());
            response.setShowInmobiAdWeightage(appInfo.getInmobiAdWeightage());
            response.setAppBgImageURL(appInfo.getAppBgImageURL());
            response.setMinIntervalInterstitial(appInfo.getMinIntervalInterstitial());
            response.setNoChildrenMsg(appInfo.getNoChildrenMsg());
            response.setVideosPerCall(appInfo.getVideosPerCall());

            ObjectMapper mapper = new ObjectMapper();
            jsonResponse = mapper.writeValueAsString(response);

            resp.setContentType("application/json");
            resp.getWriter().write(jsonResponse.toString());


        } else {
            // can we add error msg in logs that no tab in the list?
        }

        // Needs to add HomeTabDataLoader ,  NewlyAdded TabDataLoader in Tabs json object
    }


}
