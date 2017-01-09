package com.appify.vidstream.newWebApi.data;


import static com.appify.vidstream.newWebApi.Constants.DATE_ADDED_VIDEOS_ATTRIBUTE_NAME;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.appify.vidstream.newWebApi.PropertyHelper;
import com.appify.vidstream.newWebApi.PropertyNames;
import com.appify.vidstream.newWebApi.util.WebAPIUtil;
import com.appify.vidstream.newWebApi.data.AppInfo;
import com.appify.vidstream.newWebApi.data.Categorization;
import com.appify.vidstream.newWebApi.data.Entity;
import com.appify.vidstream.newWebApi.data.EntityType;
import com.appify.vidstream.newWebApi.data.OrderedVideos;
import com.appify.vidstream.newWebApi.data.Video;

public class NewlyAddedVideosCategoryLoader extends CategoryDataLoader implements Runnable {

    private static final int DEFAULT_DAYS_TO_CONSIDER = 10;
    private volatile Map<String, List<Entity>> newlyAddedVideos = new HashMap<>();

    private PropertyHelper propertyHelper;
    private AppDataLoader appDataLoader;
    private ScheduledExecutorService es;
    private WebAPIUtil webAPIUtil;

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

    @Inject
    public NewlyAddedVideosCategoryLoader(PropertyHelper propertyHelper, AppDataLoader appDataLoader, WebAPIUtil webAPIUtil) {
        this.propertyHelper = propertyHelper;
        this.appDataLoader = appDataLoader;
        this.webAPIUtil = webAPIUtil;
    }

    @Override
    public Category getTopLevelCategory() {

        Category category = new Category();
        category.setId(webAPIUtil.getImageURL(propertyHelper.getStringProperty(PropertyNames.NEWLY_ADDED_CATEGORY_ID, null)));
        category.setName(webAPIUtil.getImageURL(propertyHelper.getStringProperty(PropertyNames.NEWLY_ADDED_CATEGORY_NAME, null)));
        category.setImageURL(webAPIUtil.getImageURL(propertyHelper.getStringProperty(PropertyNames.NEWLY_ADDED_CATEGORY_IMAGE_ID, null)));
        return category;
    }

    @Override
    public List<Entity> getFirstLevelChildren(String appId) {
        return newlyAddedVideos.get(appId);
    }

    private void loadData() {
        Map<String, AppInfo> allAppsData = appDataLoader.getAppsData();

        Map<String, List<Entity>> newMap = new HashMap<>();


        Set<Map.Entry<String, AppInfo>> entrySet = allAppsData.entrySet();
        for (Map.Entry<String, AppInfo> entry : entrySet) {
            String appId = entry.getKey();
            AppInfo appInfo = entry.getValue();

            newMap.put(appId, getOrderedVideosForApp(appInfo));
        }

        newlyAddedVideos = newMap;

    }

    private List<Entity> getOrderedVideosForApp(AppInfo appInfo) {
        List<Entity> orderedVideosList = new ArrayList<>();

        List<Video> newVideos = new ArrayList<>();

        Set<String> attributes = new HashSet<>();

        List<Categorization> categorizations = appInfo.getCategorizations();

        int daysToConsider = propertyHelper.getIntProperty(PropertyNames.NEWLY_ADDED_VIDEOS_DAYS, DEFAULT_DAYS_TO_CONSIDER);

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
                            equals(DATE_ADDED_VIDEOS_ATTRIBUTE_NAME)).flatMap(child -> child.getChildren().stream()).
                            map(video -> (Video) video).filter(video -> getDaysBeforeVideoAdded(video) <= daysToConsider)
                            .collect(Collectors.toList());
                    newVideos.addAll(currNewVideos);
                } else if (childrenType != null) {
                    for (Entity child : entity.getChildren()) {
                        queue.offer(child);
                    }
                }

            }
        }

        for (String attribute : attributes) {
            VideoAttributeReverseComparator comparator = new VideoAttributeReverseComparator(attribute);
            List<Entity> sortedList = new ArrayList<>();
            sortedList.addAll(newVideos);
            Collections.sort(sortedList, comparator);

            OrderedVideos orderedVideos = new OrderedVideos();
            orderedVideos.setId(attribute);
            orderedVideos.setName(attribute);
            orderedVideos.setChildren(sortedList);
            orderedVideos.setChildType(EntityType.VIDEO);

            orderedVideosList.add(orderedVideos);
        }

        return orderedVideosList;
    }

    private static int getDaysBeforeVideoAdded(Video video) {

        return (int) (TimeUnit.DAYS.convert(new Date().getTime() - video.getDateAdded()
                .getTime(), TimeUnit.MILLISECONDS));
    }


    @Override
    public void run() {
        loadData();
    }

    @Override
    public List<Entity> getChildren(String appId, String categoryId) {
        throw new UnsupportedOperationException("Chidren cannot be exist for videos.");
    }

}
