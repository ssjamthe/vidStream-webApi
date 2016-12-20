package com.appify.vidstream.newWebApiTest;

import com.appify.vidstream.newWebApiTest.LoadAppResponse;
import com.appify.vidstream.newWebApiTest.data.*;
import com.appify.vidstream.newWebApiTest.data.jdbc.JDBCCategorizationDataLoader;
import com.appify.vidstream.newWebApiTest.data.jdbc.JDBCCategoryDataLoader;
import com.appify.vidstream.newWebApiTest.data.jdbc.JDBCOrderedVideosDataLoader;
import com.appify.vidstream.newWebApiTest.data.jdbc.JDBCVideoDataLoader;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.inject.Provider;
import com.google.inject.servlet.RequestParameters;
import com.appify.vidstream.newWebApiTest.Annotations;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

/**
 * Created by swapnil on 30/11/16.
 */
@Singleton
public class LoadChildrenServlet extends HttpServlet{

    private AppDataLoader appDataLoader;
    private Provider<Map<String, String[]>> paramsProvider;
    private List<TabDataLoader> tabDataLoaderList;
    private Entity entity;
    private EntityResp entityResp;

    @Inject
    public  LoadChildrenServlet(AppDataLoader appDataLoader,
                                @RequestParameters Provider<Map<String, String[]>> paramsProvider,
                                @Annotations.TabDataLoaders List<TabDataLoader> tabDataLoaderList){
        this.paramsProvider = paramsProvider;
        this.appDataLoader = appDataLoader;
        this.tabDataLoaderList = tabDataLoaderList;
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Map<String, AppInfo> appsInfoMap = appDataLoader.getAppsData();
        Map<String, String[]> params = paramsProvider.get();

        EntityToEntityRespConverter entityToEntityRespConverter = new EntityToEntityRespConverter();
        String jsonResponse = "";

        String deviceId = params.get("deviceId")[0];
        String appId = params.get("appId")[0];
        String tabId = params.get("tabId")[0];
        String categorizationId = null;
        String categoryId = null ;
        String orderAttribute = null;
        int pageNo = 1;
       
        
        if(params.containsKey("categorizationId")){
         categorizationId = params.get("categorizationId")[0];
        }
        if(params.containsKey("categoryId")){
         categoryId = params.get("categoryId")[0];
        }
        if(params.containsKey("orderAttribute")){
         orderAttribute = params.get("orderAttribute")[0];
        }
        if(params.containsKey("pageNo")){
         pageNo = Integer.parseInt(params.get("pageNo")[0]);
        }
        

        AppInfo appInfo = appsInfoMap.get(appId);

        int videosPerCall = appInfo.getVideosPerCall();

        LoadAppResponse response = new LoadAppResponse();
        

        if(categoryId != null){
            Map<String,Category> categoryMap = appInfo.getCategoryMap();
            entity = categoryMap.get(categoryId);
        }

        else if(categorizationId != null){
            Map<String,Categorization> categorizationMap = appInfo.getCategorizationMap();
            entity = categorizationMap.get(categorizationId);
        }
        else {
            if (!tabDataLoaderList.isEmpty()) {
                Tab tab = new Tab();
                for (TabDataLoader tabDataLoader : tabDataLoaderList) {
                   tab = tabDataLoader.getTab(appId);
                     if (tab.getId().equals(tabId)) {
                    	 entity = tabDataLoader.getTab(appId);
                    }
                }
            }
            else{
                System.out.println("tabDataLoaderList is empty..");
            }
        }
        entityResp = entityToEntityRespConverter.getEntityResp(entity,orderAttribute,pageNo,videosPerCall);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        jsonResponse = mapper.writeValueAsString(entityResp);

        resp.setContentType("application/json");
        resp.getWriter().write(jsonResponse.toString());


    }
}
