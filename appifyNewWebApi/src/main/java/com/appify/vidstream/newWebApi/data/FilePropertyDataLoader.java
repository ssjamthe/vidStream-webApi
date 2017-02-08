package com.appify.vidstream.newWebApi.data;

import com.appify.vidstream.newWebApi.Annotations;
import com.appify.vidstream.newWebApi.Constants;
import com.google.common.util.concurrent.AbstractScheduledService;


import javax.imageio.IIOException;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by swapnil on 30/11/16.
 */
@Singleton
public class FilePropertyDataLoader extends PropertyDataLoader {

    private volatile Map<String, String> props;
    private ScheduledExecutorService es;
    private String propertyFilePath;

    @Inject
    public FilePropertyDataLoader(@Annotations.PropertyFilePath String propertyFilePath) {
        this.propertyFilePath = propertyFilePath;
    }

    private void loadData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(propertyFilePath))) {
            Properties properties = new Properties();
            properties.load(reader);
            Map<String, String> propMap = new HashMap<>();
            properties.forEach((prop, val) -> propMap.put((String) prop, (String) val));
            this.props = Collections.unmodifiableMap(propMap);
        } catch (IOException ex) {
            throw new RuntimeException("Unable to load properties from file", ex);
        }
    }

    @Override
    public Map<String, String> getProps() {
        return props;
    }

    @Override
    protected void work() {
        loadData();
    }

    @Override
    protected void startUp() {
        loadData();
    }

    @Override
    protected Scheduler scheduler() {
        return Scheduler.newFixedDelaySchedule(0, 5, TimeUnit.MINUTES);
    }
}
