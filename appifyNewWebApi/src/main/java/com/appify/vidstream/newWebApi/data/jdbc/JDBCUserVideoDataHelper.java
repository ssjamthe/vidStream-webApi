package com.appify.vidstream.newWebApi.data.jdbc;

import com.appify.vidstream.newWebApi.PropertyHelper;
import com.appify.vidstream.newWebApi.PropertyNames;
import com.appify.vidstream.newWebApi.data.UserVideoDataHelper;
import com.appify.vidstream.newWebApi.data.Video;
import org.apache.log4j.Logger;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;


public class JDBCUserVideoDataHelper implements UserVideoDataHelper {

    private DataSource dataSource;
    private PropertyHelper propertyHelper;
    private static final int DEFAULT_MAX_VIDEOS_PER_APP = 20;

    static final Logger jdbcUserVideoDataHelperLogger = Logger.getLogger(JDBCUserVideoDataHelper.class);

    @Inject
    JDBCUserVideoDataHelper(DataSource dataSource, PropertyHelper propertyHelper) {
        this.dataSource = dataSource;
        this.propertyHelper = propertyHelper;
    }

    @Override
    public List<String> getRecentVideosForUser(String appId, String deviceId) {

        try (Connection con = dataSource.getConnection();) {

            String sql = "select video_id from video_viewed_user where app_id='" + appId + "' and device_id='" +
                    deviceId

                    + "' order by video_timestamp";

            PreparedStatement stmt = con.prepareStatement(sql);

            List<String> videoIds = new ArrayList<>();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                videoIds.add(rs.getString("video_id"));
            }

            return videoIds;
        } catch (SQLException ex) {
            throw new RuntimeException("Problem in updating video watched by user.", ex);
        }
    }

    @Override
    public void updateVideoWatchedByUser(String videoId, String deviceId, String appId) {

        int maxVideosToConsiderPerApp = propertyHelper.getIntProperty(PropertyNames.MAX_VIDEOS_FOR_USER_APP,
                DEFAULT_MAX_VIDEOS_PER_APP);

        try (Connection con = dataSource.getConnection();) {
            int appid = Integer.parseInt(appId);
            List<String> videosList = getVideosPerAppPerDevide(appId, deviceId);

            if (!videosList.isEmpty() && videosList.contains(videoId)) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                Date cdate = new Date();
                String strDate = sdf.format(cdate);

                String updateTimeStamp = "update video_viewed_user set video_timestamp='" + strDate + "' where " +
                        "device_id='" + deviceId + "' and video_id='" + videoId + "' and app_id=" + appid + "";
                PreparedStatement pstUpdateTime = con.prepareStatement(updateTimeStamp);
                int row = pstUpdateTime.executeUpdate();
                pstUpdateTime.close();
            } else {
                if (videosList.size() >= maxVideosToConsiderPerApp) {

                    String selectLastVidQuery = "select video_timestamp from video_viewed_user where device_id='" +
                            deviceId + "' and app_id='" + appid + "' order by video_timestamp asc limit 1";
                    PreparedStatement pstLastVid = con.prepareStatement(selectLastVidQuery);
                    ResultSet rsLastVid = pstLastVid.executeQuery();
                    rsLastVid.next();
                    String oldVidTime = rsLastVid.getString(1);
                    jdbcUserVideoDataHelperLogger.info("Old Video Timestamp = " + oldVidTime + "\n");

                    String deleteVidQuery = "delete from video_viewed_user where video_timestamp='" + oldVidTime + "'" +
                            " and device_id='" + deviceId + "' and app_id='" + appid + "'";
                    Statement pstDelete = con.createStatement();
                    pstDelete.execute(deleteVidQuery);
                    jdbcUserVideoDataHelperLogger.info(oldVidTime + " Deleted Successfully ... ");


                    String insertVideoQuery = "insert into video_viewed_user (device_id,video_id,app_id) values (?,?," +
                            "?)";
                    PreparedStatement pstInsertNewVid = con.prepareStatement(insertVideoQuery);
                    pstInsertNewVid.setString(1, deviceId);
                    pstInsertNewVid.setString(2, videoId);
                    pstInsertNewVid.setInt(3, appid);
                    int row = pstInsertNewVid.executeUpdate();
                    pstInsertNewVid.close();
                    pstLastVid.close();
                    rsLastVid.close();
                    pstDelete.close();

                } else {
                    String insertNewVideo = "insert into video_viewed_user (device_id,video_id,app_id) values (?,?,?)";
                    PreparedStatement pstInsertNewVid = con.prepareStatement(insertNewVideo);
                    pstInsertNewVid.setString(1, deviceId);
                    pstInsertNewVid.setString(2, videoId);
                    pstInsertNewVid.setInt(3, appid);
                    int row = pstInsertNewVid.executeUpdate();
                    pstInsertNewVid.close();
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Problem in updating video watched by user.", ex);
        }
    }

    /**
     * getVideosPerAppPerDevide Method will return List of Videos added per device for distinct app
     */

    private List<String> getVideosPerAppPerDevide(String appId, String deviceId) {
        List<String> videosList = new ArrayList<>();
        try (Connection con = dataSource.getConnection();) {
            String getAllVideosPerDeviceQuery = "select video_id from video_viewed_user where device_id='" + deviceId
                    + "' and app_id='" + appId + "'";

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(getAllVideosPerDeviceQuery);

            while (rs.next()) {
                String video_id = rs.getString("video_id");
                videosList.add(video_id);
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Problem in updating video watched by user.", ex);
        }
        return videosList;
    }

    @Override
    public List<String> getVideoViewedByUsersPerApp(String appId) {
        List<String> videosList = new ArrayList<String>();
        try (Connection con = dataSource.getConnection();) {
            String sql = "select distinct video_id from video_viewed_user where app_id='" + appId + "'";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                videosList.add(rs.getString("id"));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Problem in getting video watched by all users per app.", ex);
        }

        return videosList;
    }
}