package com.appify.vidstream.newWebApi.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.xml.crypto.Data;

import com.appify.vidstream.newWebApi.data.AppDataLoader;
import com.appify.vidstream.newWebApi.data.AppInfo;
import com.appify.vidstream.newWebApi.PropertyHelper;
import com.appify.vidstream.newWebApi.util.WebAPIUtil;
import com.appify.vidstream.newWebApi.PropertyNames;

public class ExploreFromDBCategoryDataLoader extends CategoryDataLoader implements Runnable {

    private AppDataLoader appDataLoader;
    private PropertyHelper propertyHelper;
    private WebAPIUtil webAPIUtil;

    private volatile Data data;
    private ScheduledExecutorService es;


    private static class Data {
        Map<String, List<Entity>> firstLevelChildrenMap;
        Map<String, AppInfo> appInfoMap;
    }

    @Inject
    public ExploreFromDBCategoryDataLoader(PropertyHelper propertyHelper, AppDataLoader appdataLoader, WebAPIUtil webAPIUtil) {
        this.appDataLoader = appdataLoader;
        this.propertyHelper = propertyHelper;
        this.webAPIUtil = webAPIUtil;
    }


    @Override
    public void startLoading() {

        loadData();
        ScheduledExecutorService es = Executors.newScheduledThreadPool(1);
        this.es = es;
        es.schedule(this, 10, TimeUnit.MINUTES);

    }

    @Override
    public void stopLoading() {
        es.shutdown();
    }


    @Override
    public void run() {
        loadData();
    }

    private void loadData() {

        Data data = new Data();
        Map<String, List<Entity>> firstLevelChildrenMap = new HashMap<>();

        Map<String, AppInfo> appInfoMap = appDataLoader.getAppsData();

        Set<Map.Entry<String, AppInfo>> appInfoEntrySet = appInfoMap.entrySet();
        for (Map.Entry<String, AppInfo> entry : appInfoEntrySet) {
            String appId = entry.getKey();
            AppInfo appInfo = entry.getValue();

            List<Entity> topLevelCategories = appInfo.getCategorizationsAsCategories().stream().map(category -> (Entity) category).collect(Collectors.toList());
            firstLevelChildrenMap.put(appId, topLevelCategories);

        }

        data.appInfoMap = appInfoMap;
        data.firstLevelChildrenMap = firstLevelChildrenMap;

        this.data = data;
    }


    @Override
    public Category getTopLevelCategory() {
        Category category = new Category();
        category.setId(webAPIUtil.getImageURL(propertyHelper.getStringProperty(PropertyNames.EXPLORE_CATEGORY_ID, null)));
        category.setName(webAPIUtil.getImageURL(propertyHelper.getStringProperty(PropertyNames.EXPLORE_CATEGORY_NAME, null)));
        category.setImageURL(webAPIUtil.getImageURL(propertyHelper.getStringProperty(PropertyNames.EXPLORE_CATEGORY_IMAGE_ID, null)));
        return category;
    }


    @Override
    public List<Entity> getFirstLevelChildren(String appId) {
        return data.firstLevelChildrenMap.get(appId);
    }

    @Override
    public List<Entity> getChildren(String appId, String categoryId) {
        Category category = data.appInfoMap.get(appId).getCategoryMap().get(categoryId);
        if (category == null) {
            return new ArrayList<>();
            //TODO : Log warning
        } else {
            return category.getChildren();
        }

    }
}
