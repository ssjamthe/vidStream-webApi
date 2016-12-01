package com.appify.vidstream.newWebApiTest.data;

import com.appify.vidstream.newWebApiTest.PropertyHelper;
import com.appify.vidstream.newWebApiTest.PropertyNames;

import javax.inject.Inject;
import java.util.stream.Collectors;

/**
 * Created by swapnil on 27/11/16.
 */
public class HomeTabDataLoader extends TabDataLoader implements Runnable {

    private PropertyHelper propertyHelper;
    private AppDataLoader appDataLoader;

    @Inject
    public HomeTabDataLoader(PropertyHelper propertyHelper, AppDataLoader appDataLoader) {
        this.propertyHelper = propertyHelper;
        this.appDataLoader = appDataLoader;
    }

    @Override
    public void startLoading() {

    }

    @Override
    public void stopLoading() {

    }


    @Override
    public Tab getTab(String appId) {

        Tab homeTab = new Tab();
        homeTab.setName("Home");
        homeTab.setId(createTabId(appId, "Home"));
        homeTab.setImageId(propertyHelper.getStringProperty(PropertyNames.HOME_TAB_IMAGE_ID, null));
        homeTab.setChildren(appDataLoader.getAppsData().get(appId).getCategorizations().
                stream().map(c -> (Entity) c).collect(Collectors.toList()));
        homeTab.setChildType(EntityType.CATEGORIZATION);

        return homeTab;
    }

    @Override
    public void run() {

    }
}
