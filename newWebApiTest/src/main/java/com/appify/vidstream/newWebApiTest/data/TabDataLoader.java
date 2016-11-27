package com.appify.vidstream.newWebApiTest.data;

/**
 * Created by swapnil on 27/11/16.
 */
public abstract class TabDataLoader {

    public abstract Tab getTab(String appId);

    public String createTabId(String appId, String tabName) {
        return appId + "_" + tabName;
    }
}
