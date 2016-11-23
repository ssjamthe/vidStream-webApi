package com.appify.vidstream.newWebApiTest.data.jdbc;

import com.appify.vidstream.newWebApiTest.PropertyHelper;
import com.appify.vidstream.newWebApiTest.PropertyNames;
import com.appify.vidstream.newWebApiTest.data.Tab;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO Remove hardcoded values outside
 */
public class JDBCTabDataLoader {

    private DataSource dataSource;
    private PropertyHelper propertyHelper;

    @Inject
    public JDBCTabDataLoader(DataSource dataSource, PropertyHelper propertyHelper) {

        this.dataSource = dataSource;
        this.propertyHelper = propertyHelper;
    }


    public List<Tab> getTabsForApp(String appId) {


        return null;
    }

    private Tab getHomeTab(String appId) {
        List<Tab> tabs = new ArrayList<Tab>();

        Tab homeTab = new Tab();
        homeTab.setName("Home");
        homeTab.setId(createTabId(appId, "Home"));
        homeTab.setImageId(propertyHelper.getStringProperty(PropertyNames.HOME_TAB_IMAGE_ID, null));

        return null;

    }

    private String createTabId(String appId, String tabName) {
        return tabName + ":" + appId;
    }
}
