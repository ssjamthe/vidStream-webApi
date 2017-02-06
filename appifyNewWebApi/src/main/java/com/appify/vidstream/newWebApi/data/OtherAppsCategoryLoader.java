package com.appify.vidstream.newWebApi.data;

import com.appify.vidstream.newWebApi.Constants;
import com.appify.vidstream.newWebApi.PropertyHelper;
import com.appify.vidstream.newWebApi.PropertyNames;
import com.appify.vidstream.newWebApi.util.WebAPIUtil;
import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.AbstractScheduledService;
import com.google.inject.Singleton;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by swapnil on 04/02/17.
 */
@Singleton
public class OtherAppsCategoryLoader extends CategoryDataLoader {

	private static final String ID = "othersApps";
	private static final String DEFAULT_OTHER_APPS_CATEGORY_NAME = "Other Apps";

	private final AppDataLoader appDataLoader;
	private final PropertyHelper propertyHelper;
	private final WebAPIUtil webAPIUtil;
	private volatile ImmutableList<Link> links;

	private void loadData() {

		ImmutableList.Builder<Link> linksBuilder = ImmutableList.builder();
		Map<String, AppInfo> appsData = appDataLoader.getAppsData();

		Set<Map.Entry<String, AppInfo>> entrySet = appsData.entrySet();
		for (Map.Entry<String, AppInfo> entry : entrySet) {
			String appId = entry.getKey();
			AppInfo appInfo = entry.getValue();
			String iconImageId = propertyHelper.getStringProperty(PropertyNames.APP_ICON_IMAGE_PREFIX + appId, null);

			if (appInfo.isActive() && iconImageId != null) {
				Link link = new Link();
				link.setId(appId);
				link.setName(appInfo.getAppName());
				link.setLinkUrl(Constants.APP_PLAYSTORE_URL_PREFIX + appId);
				link.setImageURL(webAPIUtil.getImageURL(iconImageId));
				linksBuilder.add(link);
			}
		}

		this.links = linksBuilder.build();

	}

	@Inject
	public OtherAppsCategoryLoader(AppDataLoader appDataLoader, PropertyHelper propertyHelper, WebAPIUtil webAPIUtil) {
		this.appDataLoader = appDataLoader;
		this.propertyHelper = propertyHelper;
		this.webAPIUtil = webAPIUtil;
	}

	@Override
	public Category getTopLevelCategory() {
		Category category = new Category();
		category.setId(ID);
		category.setName(propertyHelper.getStringProperty(PropertyNames.OTHER_APPS_CATEGORY_NAME,
				DEFAULT_OTHER_APPS_CATEGORY_NAME));
		category.setImageURL(webAPIUtil
				.getImageURL(propertyHelper.getStringProperty(PropertyNames.OTHER_APPS_CATEGORY_IMAGE_ID, null)));
		return category;
	}

	@Override
	public void startUp() {
		loadData();
	}

	@Override
	public EntityCollection getChildren(String appId, String categoryId, String deviceId) {
		if (ID.equals(categoryId)) {
			EntityCollection entityCollection = new EntityCollection();
			entityCollection.setEntityType(EntityType.LINK);
			ImmutableList.Builder<Link> linksBuilder = ImmutableList.builder();
			ImmutableList<Link> currLinks = this.links;
			for (Link link : currLinks) {
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
		loadData();
	}

	@Override
	protected Scheduler scheduler() {
		return Scheduler.newFixedDelaySchedule(0, 5, TimeUnit.MINUTES);
	}
}
