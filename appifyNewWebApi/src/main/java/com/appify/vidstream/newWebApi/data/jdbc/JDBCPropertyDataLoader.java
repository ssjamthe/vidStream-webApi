package com.appify.vidstream.newWebApi.data.jdbc;

import com.appify.vidstream.newWebApi.data.PropertyDataLoader;
import com.google.common.util.concurrent.AbstractScheduledService;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * TODO Remove hardcoded values outside. Inject ScheduledExecutorService.
 */
@Singleton
public class JDBCPropertyDataLoader extends PropertyDataLoader {

    private volatile Map<String, String> props;
    private DataSource dataSource;

    @Inject
    public JDBCPropertyDataLoader(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private void loadData() {

        Map<String, String> props = new HashMap<String, String>();

        try {
            Connection con = dataSource.getConnection();
            String sql = "select prop_name,prop_value from property_table";

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String propName = rs.getString("prop_name");
                String propValue = rs.getString("prop_value");

                props.put(propName, propValue);
            }
            this.props = Collections.unmodifiableMap(props);
        } catch (SQLException exp) {
            throw new RuntimeException("Unable to load properties from DB.");
        }

    }

    @Override
    public Map<String, String> getProps() {
        return props;
    }


    @Override
    protected void startUp() {
        loadData();
    }

    @Override
    protected void work() {
        loadData();
    }

    @Override
    protected Scheduler scheduler() {
        return Scheduler.newFixedDelaySchedule(0, 5, TimeUnit.MINUTES);
    }
}
