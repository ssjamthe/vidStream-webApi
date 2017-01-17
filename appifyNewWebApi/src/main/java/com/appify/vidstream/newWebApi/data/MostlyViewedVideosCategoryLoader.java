package com.appify.vidstream.newWebApi.data;

import com.appify.vidstream.newWebApi.Constants;
import com.appify.vidstream.newWebApi.PropertyHelper;
import com.appify.vidstream.newWebApi.PropertyNames;
import com.appify.vidstream.newWebApi.util.WebAPIUtil;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.MinMaxPriorityQueue;
import com.google.common.util.concurrent.AbstractScheduledService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by swapnil on 13/01/17.
 */
@Singleton
public class MostlyViewedVideosCategoryLoader extends CategoryDataLoader {

    private static final String ID = "mostlyViewed";
    private static final String DEFAULT_MOSTLY_VIEWED_CATEGORY_NAME = "Mostly Viewed";
    private static final int DEFAULT_MOSTLY_VIEWED_VIDEOS_COUNT = 50;
    private volatile ImmutableListMultimap<String, Entity> mostlyViewedVideos;

    private PropertyHelper propertyHelper;
    private AppDataLoader appDataLoader;
    private WebAPIUtil webAPIUtil;

    @Inject
    public MostlyViewedVideosCategoryLoader(PropertyHelper propertyHelper, AppDataLoader appDataLoader, WebAPIUtil
            webAPIUtil) {
        this.propertyHelper = propertyHelper;
        this.appDataLoader = appDataLoader;
        this.webAPIUtil = webAPIUtil;
    }

    @Override
    public void startUp() {
        loadData();
    }

    private void loadData() {
        Map<String, AppInfo> allAppsData = appDataLoader.getAppsData();
        ImmutableListMultimap.Builder<String, Entity> mapBuilder = ImmutableListMultimap.builder();

        Set<Map.Entry<String, AppInfo>> entrySet = allAppsData.entrySet();
        for (Map.Entry<String, AppInfo> entry : entrySet) {
            String appId = entry.getKey();
            AppInfo appInfo = entry.getValue();

            mapBuilder.putAll(appId, getOrderedVideosForApp(appInfo));
        }

        mostlyViewedVideos = mapBuilder.build();
    }

    private ImmutableList<Entity> getOrderedVideosForApp(AppInfo appInfo) {

        List<Entity> orderedVideosList = new ArrayList<>();
        List<Video> consideredVideos = new ArrayList<>();
        Set<String> attributes = new HashSet<>();

        List<Categorization> categorizations = appInfo.getCategorizations();

        int videoCount = propertyHelper.getIntProperty(PropertyNames.MOSTLY_VIEWED_VIDEOS_COUNT,
                DEFAULT_MOSTLY_VIEWED_VIDEOS_COUNT);

        MinMaxPriorityQueue<Entity> topVideosQueue = MinMaxPriorityQueue.orderedBy(new VideoAttributeReverseComparator
                (VideoAttribute.MOSTLY_VIEWED.getDataName())).maximumSize(videoCount).create();

        for (Categorization categorization : categorizations) {

            Queue<Entity> queue = new LinkedList<>();
            queue.offer(categorization);

            while (!queue.isEmpty()) {
                Entity entity = queue.poll();
                EntityType childrenType = entity.getChildType();

                if (childrenType == EntityType.ORDERED_VIDEOS) {
                    List<Entity> children = entity.getChildren();
                    attributes.addAll(children.stream().map(child -> child.getName()).collect(Collectors.toSet()));

                    List<Video> currConsideredVideos = children.stream().filter(child -> child.getName().
                            equals(VideoAttribute.MOSTLY_VIEWED.getDataName())).flatMap(child -> child.getChildren()
                            .stream()).
                            map(video -> (Video) video).collect(Collectors.toList());

                    topVideosQueue.addAll(currConsideredVideos);
                }
            }
        }

        return ImmutableList.copyOf(consideredVideos);

    }

    @Override
    public Category getTopLevelCategory() {
        Category category = new Category();
        category.setId(ID);
        category.setName(propertyHelper.getStringProperty(PropertyNames
                .MOSTLY_VIEWED_CATEGORY_NAME, DEFAULT_MOSTLY_VIEWED_CATEGORY_NAME));
        category.setImageURL(webAPIUtil.getImageURL(propertyHelper.getStringProperty(PropertyNames
                .MOSTLY_VIEWED_CATEGORY_IMAGE_ID, null)));
        return category;
    }

    @Override
    public EntityCollection getChildren(String appId, String categoryId, String deviceId) {
        if (ID.equals(categoryId)) {
            EntityCollection entityCollection = new EntityCollection();
            entityCollection.setEntityType(EntityType.ORDERED_VIDEOS);
            entityCollection.setEntities(mostlyViewedVideos.get(appId));
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
        loadData();
    }

    @Override
    protected Scheduler scheduler() {
        return Scheduler.newFixedDelaySchedule(0, 5, TimeUnit.MINUTES);
    }
}
