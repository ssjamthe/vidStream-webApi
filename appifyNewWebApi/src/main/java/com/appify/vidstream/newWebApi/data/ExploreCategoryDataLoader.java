package com.appify.vidstream.newWebApi.data;

import com.appify.vidstream.newWebApi.PropertyHelper;
import com.appify.vidstream.newWebApi.PropertyNames;
import com.appify.vidstream.newWebApi.util.WebAPIUtil;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.TimeUnit;

@Singleton
public class ExploreCategoryDataLoader extends CategoryDataLoader {

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

    @Override
    protected void work() {
        // No scheduled task.
    }

    @Override
    protected Scheduler scheduler() {
        return Scheduler.newFixedDelaySchedule(0, 5, TimeUnit.MINUTES);
    }
}
