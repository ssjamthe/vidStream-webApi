package com.appify.vidstream.newWebApi.data.jdbc;

import com.appify.vidstream.newWebApi.data.Video;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.appify.vidstream.newWebApi.Constants.DATE_ADDED_VIDEOS_ATTRIBUTE_NAME;

/**
 * Created by swapnil on 04/12/16.
 */
public class JDBCVideoDataLoader {
    private DataSource dataSource;

    @Inject
    JDBCVideoDataLoader(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Video> getVideosForCategories(String categoryId){

        try (Connection con = dataSource.getConnection();) {

            List<Video> videos = new ArrayList<Video>();
            Map<Integer, Video> videoMap = new HashMap<>();

          String sql = "select id,name,date_added from youtube_video where id in "+
                  "(select id from  youtube_video_category_mapping where category_id="+ categoryId+ ") order by date_added desc";

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("id");
                Video video = videoMap.get(id);
                if (video == null) {
                    video = new Video();
                    String name = rs.getString("name");
                    Timestamp dateAdded = rs.getTimestamp("date_added");

                    video.setId(Integer.toString(id));
                    video.setName(name);
                    video.setDateAdded(dateAdded);

                    videos.add(video);
                }
            }

            return videos;
        }catch (SQLException ex) {
            throw new RuntimeException("Problem getting video data.", ex);
        }
    }

}
