package com.appify.vidstream.newWebApi.data;


import com.appify.vidstream.newWebApi.PropertyHelper;
import com.appify.vidstream.newWebApi.PropertyNames;
import com.appify.vidstream.newWebApi.util.WebAPIUtil;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Singleton
public class NewlyAddedVideosCategoryLoader extends CategoryDataLoader {

    private static final String ID = "newlyAdded";
    private static final String DEFAULT_NEWLY_ADDED_CATEGORY_NAME = "Newly Added";
    private static final int DEFAULT_DAYS_TO_CONSIDER = 10;
    private volatile ImmutableListMultimap<String, Entity> newlyAddedVideos;

    private PropertyHelper propertyHelper;
    private AppDataLoader appDataLoader;
    private WebAPIUtil webAPIUtil;


    @Inject
    public NewlyAddedVideosCategoryLoader(PropertyHelper propertyHelper, AppDataLoader appDataLoader, WebAPIUtil
            webAPIUtil) {
        this.propertyHelper = propertyHelper;
        this.appDataLoader = appDataLoader;
        this.webAPIUtil = webAPIUtil;
    }

    @Override
    public Category getTopLevelCategory() {

        Category category = new Category();
        category.setId(ID);
        category.setName(propertyHelper.getStringProperty(PropertyNames
                .NEWLY_ADDED_CATEGORY_NAME, DEFAULT_NEWLY_ADDED_CATEGORY_NAME));
        category.setImageURL(webAPIUtil.getImageURL(propertyHelper.getStringProperty(PropertyNames
                .NEWLY_ADDED_CATEGORY_IMAGE_ID, null)));
        return category;
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

        newlyAddedVideos = mapBuilder.build();

    }

    private ImmutableList<Entity> getOrderedVideosForApp(AppInfo appInfo) {
        List<Entity> orderedVideosList = new ArrayList<>();

        List<Video> newVideos = new ArrayList<>();

        Set<String> attributes = new HashSet<>();

        List<Categorization> categorizations = appInfo.getCategorizations();

        int daysToConsider = propertyHelper.getIntProperty(PropertyNames.NEWLY_ADDED_VIDEOS_DAYS,
                DEFAULT_DAYS_TO_CONSIDER);

        for (Categorization categorization : categorizations) {

            Queue<Entity> queue = new LinkedList<>();
            queue.offer(categorization);

            while (!queue.isEmpty()) {
                Entity entity = queue.poll();
                EntityType childrenType = entity.getChildType();

                if (childrenType == EntityType.ORDERED_VIDEOS) {
                    List<Entity> children = entity.getChildren();
                    attributes.addAll(children.stream().map(child -> child.getName()).collect(Collectors.toSet()));
                    List<Video> currNewVideos = children.stream().filter(child -> child.getName().
                            equals(VideoAttribute.TIME_ADDED.getDataName())).flatMap(child -> child.getChildren()
                            .stream()).
                            map(video -> (Video) video).filter(video -> getDaysBeforeVideoAdded(video) <=
                            daysToConsider)
                            .collect(Collectors.toList());
                    newVideos.addAll(currNewVideos);
                } else if (childrenType != null) {
                    for (Entity child : entity.getChildren()) {
                        queue.offer(child);
                    }
                }

            }
        }
        return OrderedVideosListHelper.createOrderedVideosList(newVideos,attributes);
    }

    private static int getDaysBeforeVideoAdded(Video video) {

        return (int) (TimeUnit.DAYS.convert(new Date().getTime() - video.getDateAdded()
                .getTime(), TimeUnit.MILLISECONDS));
    }

    @Override
    protected void startUp() {
        loadData();
    }

    @Override
    public EntityCollection getChildren(String appId, String categoryId, String deviceId) {
        if (ID.equals(categoryId)) {
            EntityCollection entityCollection = new EntityCollection();
            entityCollection.setEntityType(EntityType.ORDERED_VIDEOS);
            entityCollection.setEntities(newlyAddedVideos.get(appId));
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
    
    public void startLoading(){
    	loadData();
    }
}
