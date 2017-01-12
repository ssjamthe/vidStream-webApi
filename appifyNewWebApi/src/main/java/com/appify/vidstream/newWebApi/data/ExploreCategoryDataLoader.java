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

public class ExploreCategoryDataLoader extends CategoryDataLoader implements Runnable {

    private static final String ID = "explore";

    private AppDataLoader appDataLoader;
    private PropertyHelper propertyHelper;
    private WebAPIUtil webAPIUtil;

    @Inject
    public ExploreCategoryDataLoader(PropertyHelper propertyHelper, AppDataLoader appdataLoader, WebAPIUtil
            webAPIUtil) {
        this.appDataLoader = appdataLoader;
        this.propertyHelper = propertyHelper;
        this.webAPIUtil = webAPIUtil;
    }


    @Override
    public void startLoading() {

    }

    @Override
    public void stopLoading() {
    }


    @Override
    public void run() {
    }


    @Override
    public Category getTopLevelCategory() {
        Category category = new Category();
        category.setId(ID);
        category.setName(propertyHelper.getStringProperty(PropertyNames.EXPLORE_CATEGORY_NAME, null));
        category.setImageURL(webAPIUtil.getImageURL(propertyHelper.getStringProperty(PropertyNames
                .EXPLORE_CATEGORY_IMAGE_ID, null)));
        return category;
    }

    @Override
    public EntityCollection getChildren(String appId, String categoryId) {
        EntityCollection entityCollection = new EntityCollection();
        if (ID.equals(categoryId)) {
            entityCollection.setEntityType(EntityType.CATEGORY);
            entityCollection.setEntities(appDataLoader.getAppsData().get(appId).getCategorizationsAsCategories());
        } else {
            Category category = appDataLoader.getAppsData().get(appId).getCategoryMap().get(categoryId);
            if (category != null) {
                entityCollection.setEntityType(category.getChildType());
                entityCollection.setEntities(category.getChildren());
            }
        }

        return entityCollection;
    }

    @Override
    public String getId() {
        return ID;
    }
}
