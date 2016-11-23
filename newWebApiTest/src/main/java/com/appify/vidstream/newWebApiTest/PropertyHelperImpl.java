package com.appify.vidstream.newWebApiTest;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by swapnil on 23/11/16.
 * TODO : Make class to load properties periodically.
 */
@Singleton
public class PropertyHelperImpl implements PropertyHelper {

    private Map<String, String> props;

    @Inject
    public PropertyHelperImpl(DataSource dataSource) {
        loadProps(dataSource);
    }

    private void loadProps(DataSource dataSource) {

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
            this.props = props;
        } catch (SQLException exp) {
            props = null;
        }


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
