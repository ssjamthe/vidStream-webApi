package com.appify.vidstream.newWebApiTest.data.jdbc;

import com.appify.vidstream.newWebApiTest.PropertyHelper;
import com.appify.vidstream.newWebApiTest.PropertyNames;
import com.appify.vidstream.newWebApiTest.data.AppDataLoader;
import com.appify.vidstream.newWebApiTest.data.AppInfo;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO Remove hardcoded values outside
 */
public class JDBCAppDataLoader implements AppDataLoader {

    private static final Integer DEFAULT_MIN_INTERVAL_INTERSTITIAL = 50; //seconds
    private static final String DEFAULT_NO_CHILDREN_MESSAGE = "No Content";
    private static final Boolean DEFAULT_SHOW_AD_MOVING_INSIDE = true;
    private static final Double DEFAULT_INMOBI_AD_WEIGHTAGE = 0.3;
    private static final Integer DEFAULT_VIDEOS_PER_CALL = 10;

    private DataSource dataSource;
    private PropertyHelper propertyHelper;
    private JDBCCategorizationDataLoader categorizationDataLoader;


    @Inject
    public JDBCAppDataLoader(DataSource dataSource, PropertyHelper propertyHelper, JDBCCategorizationDataLoader categorizationDataLoader) {

        this.dataSource = dataSource;
        this.propertyHelper = propertyHelper;
        this.categorizationDataLoader = categorizationDataLoader;
    }


    public Map<String, AppInfo> getAppsData() {

        try (Connection con = dataSource.getConnection();) {
            Map<String, AppInfo> appsData = new HashMap<String, AppInfo>();

            String sql = "select id,name,bg_image,status,date_created,date_modified from application";

            Map<String, AppInfo> appInfoMap = new HashMap<String, AppInfo>();

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {

                String appId = rs.getString("id");
                String name = rs.getString("name");
                String bgImage = rs.getString("bg_image");
                String status = rs.getString("status");
                Timestamp dateCreated = rs.getTimestamp("date_created");
                Timestamp dateModified = rs.getTimestamp("date_modified");

                AppInfo appInfo = new AppInfo();
                appInfo.setAppId(appId);
                appInfo.setAppBgImageId(bgImage);
                appInfo.setMinIntervalInterstitial(propertyHelper.getIntProperty(
                        PropertyNames.MIN_INTERVAL_INTERSTITIAL, DEFAULT_MIN_INTERVAL_INTERSTITIAL));
                appInfo.setNoChildrenMsg(propertyHelper.getStringProperty(
                        PropertyNames.NO_CHILDREN_MESSAGE, DEFAULT_NO_CHILDREN_MESSAGE));
                appInfo.setShowAdMovingInside(propertyHelper.getBooleanProperty(
                        PropertyNames.SHOW_AD_MOVING_INSIDE, DEFAULT_SHOW_AD_MOVING_INSIDE));
                appInfo.setInmobiAdWeightage(propertyHelper.getDoubleProperty(
                        PropertyNames.INMOBI_AD_WEIGHTAGE, DEFAULT_INMOBI_AD_WEIGHTAGE));
                appInfo.setVideosPerCall(
                        propertyHelper.getIntProperty(PropertyNames.VIDEOS_PER_CALL, DEFAULT_VIDEOS_PER_CALL));

                appInfo.setCategorizations(categorizationDataLoader.getCategorizationsForApp(appId));
            }

            return appsData;
        } catch (SQLException ex) {
            throw new RuntimeException("Problem getting appd data.", ex);
        }
    }


}
