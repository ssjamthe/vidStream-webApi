package com.appify.vidstream.newWebApi;

import com.appify.vidstream.newWebApi.data.AppDataLoader;
import com.appify.vidstream.newWebApi.data.CategoryDataLoader;
import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.Service;
import com.google.common.util.concurrent.ServiceManager;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * Created by swapnil on 14/01/17.
 * <p>
 * <p>This class manages services lifecycle and their dependencies.
 */
@Singleton
public class ServicesHelper {

    private AppDataLoader appDataLoader;
    private List<CategoryDataLoader> categoryDataLoaders;
    private volatile boolean initDone;
    private ServiceManager serviceManager;

    @Inject
    public void setAppDataLoader(AppDataLoader appDataLoader) {
        this.appDataLoader = appDataLoader;
    }

    @Inject
    public void setCategoryDataLoaders(List<CategoryDataLoader> categoryDataLoaders) {
        this.categoryDataLoaders = categoryDataLoaders;
    }

    public synchronized void init() {
        if (!initDone)
            return;
        try {
            ImmutableList.Builder<Service> services = ImmutableList.builder();

            services.add(appDataLoader);
            services.add(appDataLoader);
            appDataLoader.startAsync();
            appDataLoader.wait();

            for (Service categoryLoader : categoryDataLoaders) {
                services.add(categoryLoader);
                categoryLoader.startAsync();
            }

            ServiceManager serviceManager = new ServiceManager(services.build());
            serviceManager.awaitHealthy();
            this.serviceManager = serviceManager;

        } catch (InterruptedException e) {
            throw new RuntimeException("Problem starting services. " + e);
        }
    }
}
