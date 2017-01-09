package com.appify.vidstream.newWebApi.data;

import com.appify.vidstream.newWebApi.data.jdbc.JDBCPropertyDataLoader;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by swapnil on 01/12/16.
 */
public class CombinedPropertyDataLoader implements PropertyDataLoader {

    private JDBCPropertyDataLoader jdbcPropertyDataLoader;
    private FilePropertyDataLoader filePropertyDataLoader;


    @Inject
    public CombinedPropertyDataLoader(JDBCPropertyDataLoader jdbcPropertyDataLoader,
                                      FilePropertyDataLoader filePropertyDataLoader) {

        this.jdbcPropertyDataLoader = jdbcPropertyDataLoader;
        this.filePropertyDataLoader = filePropertyDataLoader;

    }

    @Override
    public void startLoading() {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public Map<String, String> getProps() {
        Map<String, String> props = new HashMap<>();
        //Database property takes precedence in case of clash.
        props.putAll(filePropertyDataLoader.getProps());
        props.putAll(jdbcPropertyDataLoader.getProps());
        return props;
    }
}
