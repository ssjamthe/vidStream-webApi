package com.appify.vidstream.newWebApiTest;

import com.appify.vidstream.newWebApiTest.data.PropertyDataLoader;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by swapnil on 23/11/16.
 */
public class PropertyHelper {

    private Map<String, String> props;


    @Inject
    public PropertyHelper(PropertyDataLoader propertyDataLoader) {
        this.props = propertyDataLoader.getProps();
    }


    public String getStringProperty(String propName, String defaultVal) {
        String propVal = props.get(propName);
        return propVal != null ? propVal : defaultVal;
    }

    public Integer getIntProperty(String propName, Integer defaultVal) {
        String propVal = props.get(propName);
        return propVal != null ? Integer.parseInt(propVal) : defaultVal;
    }

    public Boolean getBooleanProperty(String propName, Boolean defaultVal) {
        String propVal = props.get(propName);
        return propVal != null ? Boolean.parseBoolean(propVal) : defaultVal;
    }

    public Double getDoubleProperty(String propName, Double defaultVal) {
        String propVal = props.get(propName);
        return propVal != null ? Double.parseDouble(propVal) : defaultVal;
    }

}
