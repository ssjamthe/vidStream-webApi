package com.appify.vidstream.newWebApiTest.data.jdbc;

import com.appify.vidstream.newWebApiTest.data.PropertyDataLoader;

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
public class JDBCPropertyDataLoader implements PropertyDataLoader, Runnable {

    private volatile Map<String, String> props;
    private DataSource dataSource;
    private ScheduledExecutorService es;

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
    public void startLoading() {
        loadData();
        ScheduledExecutorService es = Executors.newScheduledThreadPool(1);
        this.es = es;
        es.schedule(this, 10, TimeUnit.MINUTES);
    }

    @Override
    public void stopLoading() {
        es.shutdown();
    }

    @Override
    public Map<String, String> getProps() {
        return props;
    }

    @Override
    public void run() {

        loadData();

    }

}
