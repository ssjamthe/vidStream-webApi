package com.appify.vidstream.newWebApiTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;
import javax.servlet.ServletContextEvent;
import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import com.appify.vidstream.newWebApiTest.data.AppDataLoader;
import com.appify.vidstream.newWebApiTest.data.CombinedPropertyDataLoader;
import com.appify.vidstream.newWebApiTest.data.FilePropertyDataLoader;
import com.appify.vidstream.newWebApiTest.data.HomeTabDataLoader;
import com.appify.vidstream.newWebApiTest.data.NewlyAddedTabDataLoader;
import com.appify.vidstream.newWebApiTest.data.PropertyDataLoader;
import com.appify.vidstream.newWebApiTest.data.TabDataLoader;
import com.appify.vidstream.newWebApiTest.data.jdbc.JDBCAppDataLoader;
import com.appify.vidstream.newWebApiTest.data.jdbc.JDBCPropertyDataLoader;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

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
                serve("*imageServlet").with(GetImageServlet.class);
                serve("*videoViewed").with(VideoViewServlet.class);
                serve("*feedbackForm").with(FeedbackFormServlet.class);
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

    	if(injector!=null){
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
    	}
        //Need to uncommit below code after getting the error cause
        //newlyAddedTabDataLoader.stopLoading(); 


    }
}
