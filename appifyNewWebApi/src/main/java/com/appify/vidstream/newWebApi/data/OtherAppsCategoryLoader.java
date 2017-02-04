package com.appify.vidstream.newWebApi.data;

import com.appify.vidstream.newWebApi.PropertyHelper;
import com.appify.vidstream.newWebApi.PropertyNames;
import com.appify.vidstream.newWebApi.util.WebAPIUtil;
import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.AbstractScheduledService;
import com.google.inject.Singleton;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by swapnil on 04/02/17.
 */
@Singleton
public class OtherAppsCategoryLoader extends CategoryDataLoader {

    private static final String ID = "othersWatching";
    private static final ImmutableList<Link> LINKS = createLinks();

    private final PropertyHelper propertyHelper;
    private final WebAPIUtil webAPIUtil;

    /**
     * Hardcoded apps just for now. Later we will take from DB. Currently no option in DB for app image.
     */
    private static ImmutableList<Link> createLinks() {

        ImmutableList.Builder<Link> links = ImmutableList.builder();

        Link link = new Link();
        link.setId("12");
        link.setName("Absolute Gym");
        link.setLinkUrl("https://play.google.com/store/apps/details?id=com.appify.vidstream.app_12");
        links.add(link);

        link = new Link();
        link.setId("14");
        link.setName("BeautifyU");
        link.setLinkUrl("https://play.google.com/store/apps/details?id=com.appify.vidstream.app_14");
        links.add(link);

        link = new Link();
        link.setId("18");
        link.setName("संपूर्ण पूजा");
        link.setLinkUrl("https://play.google.com/store/apps/details?id=com.appify.vidstream.app_18");
        links.add(link);

        link = new Link();
        link.setId("15");
        link.setName("Creative Home Decoration");
        link.setLinkUrl("https://play.google.com/store/apps/details?id=com.appify.vidstream.app_15");
        links.add(link);

        link = new Link();
        link.setId("21");
        link.setName("Yoga and Meditation");
        link.setLinkUrl("https://play.google.com/store/apps/details?id=com.appify.vidstream.app_21");
        links.add(link);

        link = new Link();
        link.setId("20");
        link.setName("Kids Video World");
        link.setLinkUrl("https://play.google.com/store/apps/details?id=com.appify.vidstream.app_20");
        links.add(link);

        return links.build();
    }

    @Inject
    public OtherAppsCategoryLoader(PropertyHelper propertyHelper, WebAPIUtil webAPIUtil) {
        this.propertyHelper = propertyHelper;
        this.webAPIUtil = webAPIUtil;
    }


    @Override
    public Category getTopLevelCategory() {
        Category category = new Category();
        category.setId(ID);
        category.setName(propertyHelper.getStringProperty(PropertyNames.OTHER_APPS_CATEGORY_NAME, null));
        category.setImageURL(webAPIUtil.getImageURL(propertyHelper.getStringProperty(PropertyNames
                .OTHER_APPS_CATEGORY_IMAGE_ID, null)));
        return category;
    }

    @Override
    public EntityCollection getChildren(String appId, String categoryId, String deviceId) {
        if (ID.equals(categoryId)) {
            EntityCollection entityCollection = new EntityCollection();
            entityCollection.setEntityType(EntityType.LINK);
            ImmutableList.Builder<Link> linksBuilder = ImmutableList.builder();
            for (Link link : LINKS) {
                if (!link.getId().equals(appId)) {
                    linksBuilder.add(link);
                }
            }
            entityCollection.setEntities(linksBuilder.build());
            return entityCollection;
        } else {
            throw new IllegalArgumentException("Only category supported is " + ID);
        }
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
