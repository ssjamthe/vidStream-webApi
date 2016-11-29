package com.appify.vidstream.newWebApiTest;

import com.appify.vidstream.newWebApiTest.data.*;
import com.appify.vidstream.newWebApiTest.data.jdbc.JDBCAppDataLoader;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

import javax.servlet.ServletContextEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by swapnil on 28/11/16.
 */
public class GuiceServletConfig extends GuiceServletContextListener {

    private Injector injector;

    @Override
    protected Injector getInjector() {
        Injector injector = Guice.createInjector(new ServletModule() {

            @Override
            protected void configureServlets() {
                //serve("*.html").with(MyServlet.class);

                bind(AppDataLoader.class).to(JDBCAppDataLoader.class);

            }

            @Annotations.TabDataLoaders
            List<TabDataLoader> provideTabs(HomeTabDataLoader homeTabDataLoader,
                                            NewlyAddedTabDataLoader newlyAddedTabDataLoader) {
                List<TabDataLoader> loaders = new ArrayList<TabDataLoader>();
                loaders.add(homeTabDataLoader);
                loaders.add(newlyAddedTabDataLoader);

                return loaders;
            }

        });

        AppDataLoader appDataLoader = injector.getInstance(AppDataLoader.class);
        appDataLoader.startLoading();

        PropertyDataLoader propertyDataLoader = injector.getInstance(PropertyDataLoader.class);
        propertyDataLoader.startLoading();

        HomeTabDataLoader homeTabDataLoader = injector.getInstance(HomeTabDataLoader.class);
        homeTabDataLoader.startLoading();

        NewlyAddedTabDataLoader newlyAddedTabDataLoader = injector.getInstance(NewlyAddedTabDataLoader.class);
        newlyAddedTabDataLoader.startLoading();

        this.injector = injector;
        return injector;

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

        AppDataLoader appDataLoader = injector.getInstance(AppDataLoader.class);
        appDataLoader.stopLoading();

        PropertyDataLoader propertyDataLoader = injector.getInstance(PropertyDataLoader.class);
        propertyDataLoader.stopLoading();

        HomeTabDataLoader homeTabDataLoader = injector.getInstance(HomeTabDataLoader.class);
        homeTabDataLoader.stopLoading();

        NewlyAddedTabDataLoader newlyAddedTabDataLoader = injector.getInstance(NewlyAddedTabDataLoader.class);
        newlyAddedTabDataLoader.stopLoading();


    }
}
