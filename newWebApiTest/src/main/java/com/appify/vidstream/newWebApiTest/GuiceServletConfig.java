package com.appify.vidstream.newWebApiTest;

import com.appify.vidstream.newWebApiTest.data.*;
import com.appify.vidstream.newWebApiTest.data.jdbc.JDBCAppDataLoader;
import com.appify.vidstream.newWebApiTest.data.jdbc.JDBCPropertyDataLoader;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provides;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.inject.Singleton;
import javax.servlet.Filter;
import javax.servlet.ServletContextEvent;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static com.appify.vidstream.newWebApiTest.PropertyNames.*;

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
                System.out.print("Inside GuiceServlet config");
                serve("*loadApp").with(LoadAppServlet.class);
                serve("*loadChildren").with(LoadChildrenServlet.class);
                bind(AppDataLoader.class).to(JDBCAppDataLoader.class);
                bind(PropertyDataLoader.class).to(CombinedPropertyDataLoader.class);
                bind(String.class).annotatedWith(Annotations.PropertyFilePath.class).
                        toInstance(Constants.PROPERTY_FILE_PATH);
                filter("/*").through(ValidationFilter.class);

            }

            @Provides
            @Annotations.TabDataLoaders
            List<TabDataLoader> provideTabs(HomeTabDataLoader homeTabDataLoader,
                                            NewlyAddedTabDataLoader newlyAddedTabDataLoader) {
                List<TabDataLoader> loaders = new ArrayList<TabDataLoader>();
                loaders.add(homeTabDataLoader);
                loaders.add(newlyAddedTabDataLoader);

                return loaders;
            }

            @Provides
            @Singleton
            DataSource provideDataSource(FilePropertyDataLoader filePropertyDataLoader) {
                BasicDataSource ds = new BasicDataSource();
                Map<String, String> props = filePropertyDataLoader.getProps();

                ds.setUrl(props.get(DB_URL));
                ds.setDriverClassName(props.get(DB_DRIVER_CLASS_NAME));
                ds.setUsername(props.get(DB_USER_NAME));
                ds.setPassword(props.get(DB_PASSWORD));
                ds.setMaxTotal(Integer.parseInt(props.get(DB_MAX_CONNECTIONS)));
                ds.setMaxIdle(Integer.parseInt(props.get(DB_MAX_CONNECTIONS)));

                return ds;
            }

        });

       
        
        FilePropertyDataLoader filePropertyDataLoader = injector.getInstance(FilePropertyDataLoader.class);
        filePropertyDataLoader.startLoading();

        JDBCPropertyDataLoader jdbcPropertyDataLoader = injector.getInstance(JDBCPropertyDataLoader.class);
        jdbcPropertyDataLoader.startLoading();

        CombinedPropertyDataLoader combinedPropertyDataLoader = injector.getInstance(CombinedPropertyDataLoader.class);
        combinedPropertyDataLoader.startLoading();

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

        FilePropertyDataLoader filePropertyDataLoader = injector.getInstance(FilePropertyDataLoader.class);
        //filePropertyDataLoader.startLoading();
        filePropertyDataLoader.stopLoading();

        JDBCPropertyDataLoader jdbcPropertyDataLoader = injector.getInstance(JDBCPropertyDataLoader.class);
        //jdbcPropertyDataLoader.startLoading();
        jdbcPropertyDataLoader.stopLoading();

        CombinedPropertyDataLoader combinedPropertyDataLoader = injector.getInstance(CombinedPropertyDataLoader.class);
        //combinedPropertyDataLoader.startLoading();
        combinedPropertyDataLoader.stopLoading();

        HomeTabDataLoader homeTabDataLoader = injector.getInstance(HomeTabDataLoader.class);
        homeTabDataLoader.stopLoading();

        NewlyAddedTabDataLoader newlyAddedTabDataLoader = injector.getInstance(NewlyAddedTabDataLoader.class);
        //Need to uncommit below code after getting the error cause
        //newlyAddedTabDataLoader.stopLoading(); 


    }
}
