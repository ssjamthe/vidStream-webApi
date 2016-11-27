package com.appify.vidstream.newWebApiTest.data.jdbc;

import com.appify.vidstream.newWebApiTest.PropertyHelper;
import com.appify.vidstream.newWebApiTest.PropertyNames;
import com.appify.vidstream.newWebApiTest.data.Categorization;
import com.appify.vidstream.newWebApiTest.data.Entity;
import com.appify.vidstream.newWebApiTest.data.EntityType;
import com.appify.vidstream.newWebApiTest.data.Tab;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO Remove hardcoded values outside
 */
public class JDBCTabDataLoader {

    private DataSource dataSource;
    private PropertyHelper propertyHelper;
    private JDBCCategorizationDataLoader categorizationDataLoader;

    @Inject
    public JDBCTabDataLoader(DataSource dataSource, PropertyHelper propertyHelper, JDBCCategorizationDataLoader categorizationDataLoader) {

        this.dataSource = dataSource;
        this.propertyHelper = propertyHelper;
        this.categorizationDataLoader = categorizationDataLoader;
    }


    public List<Tab> getTabsForApp(String appId) {

        List<Categorization> categorizations = categorizationDataLoader.getCategorizationsForApp(appId);
        List<Tab> tabs = new ArrayList<>();
        tabs.add(getHomeTab(appId, categorizations));

        return tabs;
    }

    private Tab getHomeTab(String appId, List<Categorization> categorizations) {

        Tab homeTab = new Tab();
        homeTab.setName("Home");
        homeTab.setId(createTabId(appId, "Home"));
        homeTab.setImageId(propertyHelper.getStringProperty(PropertyNames.HOME_TAB_IMAGE_ID, null));
        homeTab.setChildren(categorizations.stream().map(c -> (Entity) c).collect(Collectors.toList()));
        homeTab.setChildType(EntityType.CATEGORIZATION);

        return homeTab;

    }

    private String createTabId(String appId, String tabName) {
        return tabName + ":" + appId;
    }
}
