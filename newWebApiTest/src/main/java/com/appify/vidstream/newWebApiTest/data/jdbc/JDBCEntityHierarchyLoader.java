package com.appify.vidstream.newWebApiTest.data.jdbc;

import com.appify.vidstream.newWebApiTest.data.AllAppsData;
import com.appify.vidstream.newWebApiTest.data.EntityHierarchyLoader;

import javax.inject.Inject;

/**
 * Created by swapnil on 23/11/16.
 */
public class JDBCEntityHierarchyLoader implements EntityHierarchyLoader, Runnable {

    private JDBCAppDataLoader appDataLoader;
    private volatile AllAppsData allAppsData;


    @Inject
    public JDBCEntityHierarchyLoader(JDBCAppDataLoader appDataLoader) {
        this.appDataLoader = appDataLoader;
    }

    public void start() {

    }

    public void stop() {

    }

    public AllAppsData getAllAppsData() {
        return null;
    }

    public void run() {

    }

    private void loadData() {


    }


}
