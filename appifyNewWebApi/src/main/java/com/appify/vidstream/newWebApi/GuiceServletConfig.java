package com.appify.vidstream.newWebApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;
import javax.servlet.ServletContextEvent;
import javax.sql.DataSource;

import com.appify.vidstream.newWebApi.data.*;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.Logger;

import com.appify.vidstream.newWebApi.data.jdbc.JDBCAppDataLoader;
import com.appify.vidstream.newWebApi.data.jdbc.JDBCPropertyDataLoader;
import com.appify.vidstream.newWebApi.data.jdbc.JDBCUserVideoDataHelper;
import com.appify.vidstream.newWebApi.Annotations;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

/**
 * Created by swapnil on 28/11/16.
 * TODO : Move functionalities to other classes.
 */
public class GuiceServletConfig extends GuiceServletContextListener {

    private Injector injector;
    static final Logger guiceServletConfigLogger = Logger.getLogger(GuiceServletConfig.class);

    @Override
    protected Injector getInjector() {
        Injector injector = Guice.createInjector(new ServletModule() {

            @Override
            protected void configureServlets() {
                System.out.print("Inside GuiceServlet config");
                serve("*loadApp").with(LoadAppServlet.class);
                serve("*imageServlet").with(GetImageServlet.class);
                serve("*videoViewed").with(VideoViewedServlet.class);
                serve("*feedbackForm").with(FeedbackFormServlet.class);
                serve("*loadChildrenForCategories").with(LoadChildrenForCategoryServlet.class);
                bind(AppDataLoader.class).to(JDBCAppDataLoader.class);
                bind(UserVideoDataHelper.class).to(JDBCUserVideoDataHelper.class);
                bind(PropertyDataLoader.class).to(CombinedPropertyDataLoader.class);
                bind(String.class).annotatedWith(Annotations.PropertyFilePath.class).
                        toInstance(Constants.PROPERTY_FILE_PATH);
                filter("/*").through(ValidationFilter.class);

            }

            @Provides
            @Annotations.CategoryDataLoaders
            List<CategoryDataLoader> provideTabs(ExploreCategoryDataLoader exploreCategoryDataLoader,
                                                 MostlyViewedVideosCategoryLoader mostlyViewedVideosCategoryLoader,
                                                 NewlyAddedVideosCategoryLoader newlyAddedTabDataLoader,
                                                 OthersWatchingVideosCategoryLoader othersWatchingVideosCategoryLoader,
                                                 RecentlyViewedVideosCategoryLoader recentlyViewedVideosCategoryLoader,
                                                 OtherAppsCategoryLoader otherAppsCategoryLoader) {
                List<CategoryDataLoader> loaders = new ArrayList<CategoryDataLoader>();
                loaders.add(exploreCategoryDataLoader);
                loaders.add(mostlyViewedVideosCategoryLoader);
                loaders.add(newlyAddedTabDataLoader);
                loaders.add(othersWatchingVideosCategoryLoader);
                loaders.add(recentlyViewedVideosCategoryLoader);
                loaders.add(otherAppsCategoryLoader);

                return loaders;
            }

            @Provides
            @Singleton
            DataSource provideDataSource(FilePropertyDataLoader filePropertyDataLoader) {
                BasicDataSource ds = new BasicDataSource();
                Map<String, String> props = filePropertyDataLoader.getProps();

                ds.setUrl(props.get(PropertyNames.DB_URL));
                ds.setDriverClassName(props.get(PropertyNames.DB_DRIVER_CLASS_NAME));
                ds.setUsername(props.get(PropertyNames.DB_USER_NAME));
                ds.setPassword(props.get(PropertyNames.DB_PASSWORD));
                ds.setMaxTotal(Integer.parseInt(props.get(PropertyNames.DB_MAX_CONNECTIONS)));
                ds.setMaxIdle(Integer.parseInt(props.get(PropertyNames.DB_MAX_CONNECTIONS)));

                return ds;
            }


        });

        FilePropertyDataLoader filePropertyDataLoader = injector.getInstance(FilePropertyDataLoader.class);
        filePropertyDataLoader.startAsync();
        filePropertyDataLoader.awaitRunning();

        JDBCPropertyDataLoader jdbcPropertyDataLoader = injector.getInstance(JDBCPropertyDataLoader.class);
        jdbcPropertyDataLoader.startAsync();
        jdbcPropertyDataLoader.awaitRunning();

        CombinedPropertyDataLoader combinedPropertyDataLoader = injector.getInstance(CombinedPropertyDataLoader.class);
        combinedPropertyDataLoader.startAsync();
        combinedPropertyDataLoader.awaitRunning();

        AppDataLoader appDataLoader = injector.getInstance(AppDataLoader.class);
        appDataLoader.startAsync();
        appDataLoader.awaitRunning();

        ExploreCategoryDataLoader exploreCategoryDataLoader = injector.getInstance
                (ExploreCategoryDataLoader.class);
        exploreCategoryDataLoader.startAsync();
        exploreCategoryDataLoader.awaitRunning();

        MostlyViewedVideosCategoryLoader mostlyViewedVideosCategoryLoader = injector.getInstance
                (MostlyViewedVideosCategoryLoader.class);
        mostlyViewedVideosCategoryLoader.startAsync();
        mostlyViewedVideosCategoryLoader.awaitRunning();

        NewlyAddedVideosCategoryLoader newlyAddedVideosCategoryLoader = injector.getInstance
                (NewlyAddedVideosCategoryLoader.class);
        newlyAddedVideosCategoryLoader.startAsync();
        newlyAddedVideosCategoryLoader.awaitRunning();

        OthersWatchingVideosCategoryLoader othersWatchingVideosCategoryLoader = injector.getInstance
                (OthersWatchingVideosCategoryLoader.class);
        othersWatchingVideosCategoryLoader.startAsync();
        othersWatchingVideosCategoryLoader.awaitRunning();

        RecentlyViewedVideosCategoryLoader recentlyViewedVideosCategoryLoader = injector.getInstance
                (RecentlyViewedVideosCategoryLoader.class);
        recentlyViewedVideosCategoryLoader.startAsync();
        recentlyViewedVideosCategoryLoader.awaitRunning();

        OtherAppsCategoryLoader otherAppsCategoryLoader = injector.getInstance
                (OtherAppsCategoryLoader.class);
        otherAppsCategoryLoader.startAsync();
        otherAppsCategoryLoader.awaitRunning();


        this.injector = injector;
        return injector;

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

        if (injector != null) {
            OtherAppsCategoryLoader otherAppsCategoryLoader = injector.getInstance
                    (OtherAppsCategoryLoader.class);
            otherAppsCategoryLoader.stopAsync();
            otherAppsCategoryLoader.awaitTerminated();

            RecentlyViewedVideosCategoryLoader recentlyViewedVideosCategoryLoader = injector.getInstance
                    (RecentlyViewedVideosCategoryLoader.class);
            recentlyViewedVideosCategoryLoader.stopAsync();
            recentlyViewedVideosCategoryLoader.awaitTerminated();

            OthersWatchingVideosCategoryLoader othersWatchingVideosCategoryLoader = injector.getInstance
                    (OthersWatchingVideosCategoryLoader.class);
            othersWatchingVideosCategoryLoader.stopAsync();
            othersWatchingVideosCategoryLoader.awaitTerminated();

            NewlyAddedVideosCategoryLoader newlyAddedVideosCategoryLoader = injector.getInstance
                    (NewlyAddedVideosCategoryLoader.class);
            newlyAddedVideosCategoryLoader.stopAsync();
            newlyAddedVideosCategoryLoader.awaitTerminated();

            MostlyViewedVideosCategoryLoader mostlyViewedVideosCategoryLoader = injector.getInstance
                    (MostlyViewedVideosCategoryLoader.class);
            mostlyViewedVideosCategoryLoader.stopAsync();
            mostlyViewedVideosCategoryLoader.awaitTerminated();

            ExploreCategoryDataLoader exploreCategoryDataLoader = injector.getInstance
                    (ExploreCategoryDataLoader.class);
            exploreCategoryDataLoader.stopAsync();
            exploreCategoryDataLoader.awaitTerminated();

            AppDataLoader appDataLoader = injector.getInstance(AppDataLoader.class);
            appDataLoader.stopAsync();
            appDataLoader.awaitTerminated();

            CombinedPropertyDataLoader combinedPropertyDataLoader = injector.getInstance(CombinedPropertyDataLoader
                    .class);
            combinedPropertyDataLoader.stopAsync();
            combinedPropertyDataLoader.awaitTerminated();

            JDBCPropertyDataLoader jdbcPropertyDataLoader = injector.getInstance(JDBCPropertyDataLoader.class);
            jdbcPropertyDataLoader.stopAsync();
            jdbcPropertyDataLoader.awaitTerminated();

            FilePropertyDataLoader filePropertyDataLoader = injector.getInstance(FilePropertyDataLoader.class);
            filePropertyDataLoader.stopAsync();
            filePropertyDataLoader.awaitTerminated();

        }


    }
}
