package com.appify.vidstream.newWebApiTest.data;

import com.appify.vidstream.newWebApiTest.PropertyHelper;
import com.appify.vidstream.newWebApiTest.PropertyNames;

import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.appify.vidstream.newWebApiTest.Constants.DATE_ADDED_VIDEOS_ATTRIBUTE_NAME;

/**
 * TODO : Inject scheduler service too.
 */
public class NewlyAddedTabDataLoader extends TabDataLoader implements Runnable {

    private static final int DEFAULT_DAYS_TO_CONSIDER = 10;
    private volatile Map<String, List<OrderedVideos>> newlyAddedVideos = new HashMap<>();

    private PropertyHelper propertyHelper;
    private AppDataLoader appDataLoader;
    private ScheduledExecutorService es;

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
    public NewlyAddedTabDataLoader(PropertyHelper propertyHelper, AppDataLoader appDataLoader) {
        this.propertyHelper = propertyHelper;
        this.appDataLoader = appDataLoader;
    }

    @Override
    public Tab getTab(String appId) {
        Tab tab = new Tab();
        tab.setName("Newly Added");
        tab.setId(createTabId(appId, "NewlyAdded"));
        tab.setImageId(propertyHelper.getStringProperty(PropertyNames.HOME_TAB_IMAGE_ID, null));
        tab.setChildren(newlyAddedVideos.get(appId).
                stream().map(c -> (Entity) c).collect(Collectors.toList()));
        tab.setChildType(EntityType.ORDERED_VIDEOS);

        return tab;
    }

    private void loadData() {
        Map<String, AppInfo> allAppsData = appDataLoader.getAppsData();

        Map<String, List<OrderedVideos>> newMap = new HashMap<>();


        Set<Map.Entry<String, AppInfo>> entrySet = allAppsData.entrySet();
        for (Map.Entry<String, AppInfo> entry : entrySet) {
            String appId = entry.getKey();
            AppInfo appInfo = entry.getValue();

            newMap.put(appId, getOrderedVideosForApp(appInfo));
        }

        newlyAddedVideos = newMap;

    }

    private List<OrderedVideos> getOrderedVideosForApp(AppInfo appInfo) {
        List<OrderedVideos> orderedVideosList = new ArrayList<>();

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
                            equals(DATE_ADDED_VIDEOS_ATTRIBUTE_NAME)).map(child -> child.getChildren()).
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
            Collections.sort(newVideos, comparator);

            OrderedVideos orderedVideos = new OrderedVideos();
            orderedVideos.setId(attribute);
            orderedVideos.setName(attribute);
            orderedVideos.setChildren(sortedList);
            orderedVideos.setChildType(EntityType.VIDEO);
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
}

