package com.appify.vidstream.newWebApi.data;

import com.appify.vidstream.newWebApi.PropertyHelper;
import com.appify.vidstream.newWebApi.PropertyNames;
import com.appify.vidstream.newWebApi.util.WebAPIUtil;
import com.google.common.collect.ImmutableList;

import javax.inject.Inject;
import javax.inject.Singleton;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Singleton
public class ExploreCategoryDataLoader extends CategoryDataLoader {

	private static final String ID = "explore";
	private static final String DEFAULT_EXPLORE_CATEGORY_NAME = "Explore";

	private AppDataLoader appDataLoader;
	private PropertyHelper propertyHelper;
	private WebAPIUtil webAPIUtil;

	@Inject
	public ExploreCategoryDataLoader(PropertyHelper propertyHelper, AppDataLoader appdataLoader,
			WebAPIUtil webAPIUtil) {
		this.appDataLoader = appdataLoader;
		this.propertyHelper = propertyHelper;
		this.webAPIUtil = webAPIUtil;
	}

	@Override
	public Category getTopLevelCategory() {
		Category category = new Category();
		category.setId(ID);
		category.setName(
				propertyHelper.getStringProperty(PropertyNames.EXPLORE_CATEGORY_NAME, DEFAULT_EXPLORE_CATEGORY_NAME));
		category.setImageURL(webAPIUtil
				.getImageURL(propertyHelper.getStringProperty(PropertyNames.EXPLORE_CATEGORY_IMAGE_ID, null)));
		return category;
	}

	@Override
	public EntityCollection getChildren(String appId, String categoryId, String deviceId) {
		EntityCollection entityCollection = new EntityCollection();
		if (ID.equals(categoryId)) {
			entityCollection.setEntityType(EntityType.CATEGORY);
			ImmutableList<Category> catagorizationsAsCategories = appDataLoader.getAppsData().get(appId)
					.getCategorizationsAsCategories();
			// If there is single categorization we ignore it.
			if (catagorizationsAsCategories.size() == 1) {
				String categorizationAsCategoryId = catagorizationsAsCategories.get(0).getId();
				Category category = appDataLoader.getAppsData().get(appId).getCategoryMap().get(categorizationAsCategoryId);
				if (category != null) {
					entityCollection.setEntityType(category.getChildType());
					entityCollection.setEntities(category.getChildren());
				}
			} else {
				entityCollection.setEntities(catagorizationsAsCategories);
			}
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
