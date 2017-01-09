package com.appify.vidstream.newWebApi;

import com.appify.vidstream.newWebApi.LoadAppResponse;
import com.appify.vidstream.newWebApi.data.*;
import com.appify.vidstream.newWebApi.data.jdbc.JDBCCategoryDataLoader;
import com.appify.vidstream.newWebApi.data.jdbc.JDBCOrderedVideosDataLoader;
import com.appify.vidstream.newWebApi.data.jdbc.JDBCVideoDataLoader;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.inject.Provider;
import com.google.inject.servlet.RequestParameters;
import com.appify.vidstream.newWebApi.Annotations;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

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
    private Entity entity;
    private EntityResp entityResp;
    
    static final Logger loadChildrenServletLogger = Logger.getLogger(LoadChildrenServlet.class);

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

       
        entityResp = entityToEntityRespConverter.getEntityResp(entity,orderAttribute,pageNo,videosPerCall);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        jsonResponse = mapper.writeValueAsString(entityResp);

        resp.setContentType("application/json");
        resp.getWriter().write(jsonResponse.toString());


    }
}
