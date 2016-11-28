package com.appify.vidstream.newWebApiTest;

import com.appify.vidstream.newWebApiTest.data.AppDataLoader;
import com.appify.vidstream.newWebApiTest.data.jdbc.JDBCAppDataLoader;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

/**
 * Created by swapnil on 28/11/16.
 */
public class GuiceServletConfig extends GuiceServletContextListener {

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new ServletModule() {

            @Override
            protected void configureServlets() {
                //serve("*.html").with(MyServlet.class);

                bind(AppDataLoader.class).to(JDBCAppDataLoader.class);

            }

        });
    }
}
