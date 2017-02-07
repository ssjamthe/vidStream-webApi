package com.appify.vidstream.newWebApi.data;

import com.appify.vidstream.newWebApi.PropertyHelper;
import com.appify.vidstream.newWebApi.PropertyNames;
import com.appify.vidstream.newWebApi.util.WebAPIUtil;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by swapnil on 17/01/17.
 */
@Singleton
public class RecentlyViewedVideosCategoryLoader extends CategoryDataLoader {

    private static final String ID = "recentlyViewed";
    private static final String DEFAULT_RECENTLY_VIEWED_CATEGORY_NAME = "Recently Viewed";


    private final PropertyHelper propertyHelper;
    private final AppDataLoader appDataLoader;
    private final WebAPIUtil webAPIUtil;
    private UserVideoDataHelper userVideoDataHelper;


    @Inject
    public RecentlyViewedVideosCategoryLoader(PropertyHelper propertyHelper, AppDataLoader appDataLoader, WebAPIUtil
            webAPIUtil, UserVideoDataHelper userVideoDataHelper) {
        this.propertyHelper = propertyHelper;
        this.appDataLoader = appDataLoader;
        this.webAPIUtil = webAPIUtil;
        this.userVideoDataHelper = userVideoDataHelper;
    }

    @Override
    public Category getTopLevelCategory() {
        Category category = new Category();
        category.setId(ID);
        category.setName(propertyHelper.getStringProperty(PropertyNames
                .RECENTLY_VIEWED_CATEGORY_NAME, DEFAULT_RECENTLY_VIEWED_CATEGORY_NAME));
        category.setImageURL(webAPIUtil.getImageURL(propertyHelper.getStringProperty(PropertyNames
                .RECENTLY_VIEWED_CATEGORY_IMAGE_ID, null)));
        return category;
    }

    @Override
    public EntityCollection getChildren(String appId, String categoryId, String deviceId) {

        if (ID.equals(categoryId)) {

            List<String> videoIds = userVideoDataHelper.getRecentVideosForUser(appId, deviceId);
            Map<String, AppInfo> appInfoMap = appDataLoader.getAppsData();
            AppInfo appInfo = appInfoMap.get(appId);
            ImmutableMap<String, Video> videoMap = appInfo.getVideoMap();
            Set<String> attributeNames = new HashSet<>();

            int currrAttributeVal = videoIds.size();
            List<Video> videos = new ArrayList<>();

            for (String videoId : videoIds) {
                Video video = videoMap.get(videoId);
                if (video != null) {
                	attributeNames.addAll(video.getAttributeValues().keySet());
                    Video newVideoObj = new Video(video);
                    newVideoObj.getAttributeValues().put(VideoAttribute.RECENTLY_VIEWED.getDataName(),
                            currrAttributeVal);
                    currrAttributeVal--;
                    videos.add(newVideoObj);
                }
            }

            ImmutableList<Entity> orderedVideosList = OrderedVideosListHelper.createOrderedVideosList(videos,
                    attributeNames);
            EntityCollection entityCollection = new EntityCollection();
            entityCollection.setEntityType(EntityType.ORDERED_VIDEOS);
            entityCollection.setEntities(orderedVideosList);

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

    }

    @Override
    protected Scheduler scheduler() {
        return Scheduler.newFixedDelaySchedule(0, 5, TimeUnit.MINUTES);
    }
}
