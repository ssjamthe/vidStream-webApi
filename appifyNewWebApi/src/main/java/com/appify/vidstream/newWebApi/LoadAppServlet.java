package com.appify.vidstream.newWebApi;

import com.appify.vidstream.newWebApi.data.*;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Provider;
import com.google.inject.servlet.RequestParameters;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by swapnil on 28/11/16.
 */
@Singleton
public class LoadAppServlet extends HttpServlet {

    private AppDataLoader appDataLoader;
    private Provider<Map<String, String[]>> paramsProvider;
    private List<CategoryDataLoader> categoryLoaders;

    private static final Logger loadAppServletLogger = Logger.getLogger(LoadAppServlet.class);

    @Inject
    public LoadAppServlet(AppDataLoader appDataLoader,
                          @RequestParameters Provider<Map<String, String[]>> paramsProvider,
                          @Annotations.CategoryDataLoaders List<CategoryDataLoader> categoryLoaders) {
        this.appDataLoader = appDataLoader;
        this.paramsProvider = paramsProvider;
        this.categoryLoaders = categoryLoaders;
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        RRLogs rrLogs = new RRLogs();
        long startTime = System.currentTimeMillis();
        Map<String, AppInfo> appsInfoMap = appDataLoader.getAppsData();

        Map<String, String[]> params = paramsProvider.get();

        String appId = params.get("appId")[0];

        AppInfo appInfo = appsInfoMap.get(appId);

        String installTimestamp;
        if(params.get("installTimestamp") != null)
        	installTimestamp = params.get("installTimestamp")[0];
        else
        	installTimestamp = null;
        String deviceId = params.get("deviceId")[0];
        LoadAppResponse response = new LoadAppResponse();

        String authenticationError = "Authentication Success";

        CategoryToCategoryRespConverter categoryToCategoryRespConverter = new CategoryToCategoryRespConverter();
        CategoryResp[] categoryResps = new CategoryResp[categoryLoaders.size()];

        int i = 0;
        for (CategoryDataLoader categoryDataLoader : categoryLoaders) {
            Category category = categoryDataLoader.getTopLevelCategory();
            CategoryResp categoryResp = (CategoryResp) (categoryToCategoryRespConverter
                    .getCategoryRespFromCategoryWithoutChild(categoryDataLoader.getId(), category));
            categoryResps[i] = categoryResp;
            i++;
        }

        CategorizationResp selectedCategorization = new CategorizationResp();
        selectedCategorization.setId("homeCategorizationId");
        selectedCategorization.setName("Home");
        selectedCategorization.setCategories(categoryResps);

        CategorizationResp homeCategorizationWithoutChildren = new CategorizationResp();
        homeCategorizationWithoutChildren.setId("homeCategorizationId");
        homeCategorizationWithoutChildren.setName("Home");

        CategorizationResp[] categorizationResps = new CategorizationResp[1];
        categorizationResps[0] = homeCategorizationWithoutChildren;

        response.setCategorizations(categorizationResps);
        response.setSelectedCategorization(selectedCategorization);
        response.setShowAdMovingInside(appInfo.isShowAdMovingInside());
        response.setShowBanner(appInfo.isShowBanner());
        response.setShowInmobiAdWeightage(appInfo.getInmobiAdWeightage());
        response.setAppBgImageURL(appInfo.getAppBgImageURL());
        response.setMinIntervalInterstitial(appInfo.getMinIntervalInterstitial());
        response.setNoContentMessage(appInfo.getNoChildrenMsg());
        response.setVideosPerCall(appInfo.getVideosPerCall());

        ObjectMapper mapper = new ObjectMapper();
        String jsonResponse = mapper.writeValueAsString(response);
        
        // Sending data to RRLogs
        String apiname = "loadApp.java";
        String requestparam = "{" + "appId=" + appId
                + ", installTimestamp=" + installTimestamp
                + ", deviceId=" + deviceId + "}";
        long endTime = System.currentTimeMillis();
        long duration = endTime-startTime;
        String responseTime = duration + " msec.";
        rrLogs.getLoadAppData(apiname, requestparam, jsonResponse, responseTime, authenticationError);

        resp.setContentType("application/json");
        resp.getWriter().write(jsonResponse.toString());

    }

    // Needs to add HomeTabDataLoader , NewlyAdded TabDataLoader in Tabs json
    // object
}
