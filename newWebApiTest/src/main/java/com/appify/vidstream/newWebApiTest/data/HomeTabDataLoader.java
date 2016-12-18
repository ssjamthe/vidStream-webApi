package com.appify.vidstream.newWebApiTest.data;

import com.appify.vidstream.newWebApiTest.PropertyHelper;
import com.appify.vidstream.newWebApiTest.PropertyNames;
import com.appify.vidstream.newWebApiTest.util.WebAPIUtil;

import javax.inject.Inject;
import java.util.stream.Collectors;

/**
 * Created by swapnil on 27/11/16.
 */
public class HomeTabDataLoader extends TabDataLoader implements Runnable {

    private PropertyHelper propertyHelper;
    private AppDataLoader appDataLoader;
    private WebAPIUtil  webAPIUtil;

    @Inject
    public HomeTabDataLoader(PropertyHelper propertyHelper, AppDataLoader appDataLoader,WebAPIUtil  webAPIUtil) {
        this.propertyHelper = propertyHelper;
        this.appDataLoader = appDataLoader;
        this.webAPIUtil=webAPIUtil;
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
        homeTab.setImageURL(webAPIUtil.getImageURL(propertyHelper.getStringProperty(PropertyNames.HOME_TAB_IMAGE_ID, null)));
        homeTab.setChildren(appDataLoader.getAppsData().get(appId).getCategorizations().
                stream().map(c -> (Entity) c).collect(Collectors.toList()));
        homeTab.setChildType(EntityType.CATEGORIZATION);

        return homeTab;
    }

    @Override
    public void run() {

    }
}
