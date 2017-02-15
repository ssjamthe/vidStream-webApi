package com.appify.vidstream.newWebApi.data;

import com.appify.vidstream.newWebApi.PropertyHelper;
import com.appify.vidstream.newWebApi.PropertyNames;
import com.appify.vidstream.newWebApi.util.WebAPIUtil;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMap;
import org.apache.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by Kalyani on 17/01/17.
 */
@Singleton
public class OthersWatchingVideosCategoryLoader extends CategoryDataLoader {

	private static final String ID = "othersWatching";
	private static final String DEFAULT_OTHER_WATCHING_CATEGORY_NAME = "Others Watching";

	private AppDataLoader appDataLoader;
	private PropertyHelper propertyHelper;
	private WebAPIUtil webAPIUtil;
	private UserVideoDataHelper userVideoDataHelper;

	private volatile ImmutableListMultimap<String, Entity> othersWatchingVideos;

	static final Logger othersWatchingVideosLogger = Logger.getLogger(OthersWatchingVideosCategoryLoader.class);

	@Inject
	public OthersWatchingVideosCategoryLoader(AppDataLoader appDataLoader, PropertyHelper propertyHelper,
			WebAPIUtil webAPIUtil, UserVideoDataHelper userVideoDataHelper) {
		this.appDataLoader = appDataLoader;
		this.propertyHelper = propertyHelper;
		this.webAPIUtil = webAPIUtil;
		this.userVideoDataHelper = userVideoDataHelper;

	}

	private void loadData() {
		Map<String, AppInfo> allAppsData = appDataLoader.getAppsData();
		ImmutableListMultimap.Builder<String, Entity> mapBuilder = ImmutableListMultimap.builder();

		Set<Map.Entry<String, AppInfo>> entrySet = allAppsData.entrySet();
		Set<String> attributeNames = new HashSet<>();

		for (Map.Entry<String, AppInfo> entry : entrySet) {
			String appId = entry.getKey();
			AppInfo appInfo = entry.getValue();

			ImmutableMap<String, Video> videoMap = appInfo.getVideoMap();

			List<String> videoIds = userVideoDataHelper.getVideoViewedByUsersPerApp(appId);
			List<Video> videos = new ArrayList<Video>();

			for (String videoId : videoIds) {
				Video video = videoMap.get(videoId);
				if (video == null) {
					othersWatchingVideosLogger
							.warn("Video info for video id : " + videoId + " is not present in " + "AppInfo Object...");
					continue;
				}
				attributeNames.addAll(video.getAttributeValues().keySet());
				videos.add(video);
			}
			mapBuilder.putAll(appId, OrderedVideosListHelper.createOrderedVideosList(videos, attributeNames));
		}

		othersWatchingVideos = mapBuilder.build();
	}

	public void startUp() {
		loadData();
	}

	@Override
	public Category getTopLevelCategory() {
		Category category = new Category();
		category.setId(ID);
		category.setName(propertyHelper.getStringProperty(PropertyNames.OTHERS_WATCHING_CATEGORY_NAME,
				DEFAULT_OTHER_WATCHING_CATEGORY_NAME));
		category.setImageURL(webAPIUtil
				.getImageURL(propertyHelper.getStringProperty(PropertyNames.OTHERS_WATCHING_CATEGORY_IMAGE_ID, null)));
		return category;
	}

	@Override
	public EntityCollection getChildren(String appId, String categoryId, String deviceId) {
		if (ID.equals(categoryId)) {
			EntityCollection entityCollection = new EntityCollection();
			entityCollection.setEntityType(EntityType.ORDERED_VIDEOS);
			entityCollection.setEntities(othersWatchingVideos.get(appId));
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
