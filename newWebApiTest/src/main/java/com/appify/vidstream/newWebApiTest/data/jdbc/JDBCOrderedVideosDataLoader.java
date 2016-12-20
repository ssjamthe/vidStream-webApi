package com.appify.vidstream.newWebApiTest.data.jdbc;

import com.appify.vidstream.newWebApiTest.data.EntityType;
import com.appify.vidstream.newWebApiTest.data.OrderedVideos;
import com.appify.vidstream.newWebApiTest.data.Video;
import com.appify.vidstream.newWebApiTest.data.VideoAttributeReverseComparator;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static com.appify.vidstream.newWebApiTest.Constants.DATE_ADDED_VIDEOS_ATTRIBUTE_NAME;

/**
 * Created by swapnil on 25/11/16.
 */
public class JDBCOrderedVideosDataLoader {

    private DataSource dataSource;

    @Inject
    JDBCOrderedVideosDataLoader(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    public List<OrderedVideos> getOrderedVideosForCategory(String categoryId) {

        try (Connection con = dataSource.getConnection();) {

            List<OrderedVideos> orderedVideosList = new ArrayList<>();
            Map<Integer, Video> videoMap = new HashMap<>();


            //Attribute names must be unique
            Set<String> attributeNames = new HashSet<>();
            List<Video> videos = new ArrayList<>();
            String sql = "Select yv.id as id,yv.name as name,yv.date_added as date_added," +
                    "vav.attribute_id as attribute_id,va.name as attribute_name,vav.value as attribute_value from youtube_video yv" +
                    " inner join video_attribute_value vav on yv.id = vav.video_id inner join video_attribute va on vav.attribute_id=va.id where yv.id " +
                    "in (select video_id from youtube_video_category_mapping " +
                    "where category_id=" + categoryId + ")";

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                //int id = rs.getInt("id");
                String id  = rs.getString("id");
                Video video = videoMap.get(id);
                if (video == null) {
                    video = new Video();
                    String name = rs.getString("name");
                    Timestamp dateAdded = rs.getTimestamp("date_added");

//                    video.setId(Integer.toString(id));
                    video.setId(id);
                    video.setName(name);
                    video.setDateAdded(dateAdded);
                    Map<String, Integer> attributeValues = new HashMap<String, Integer>();
                    attributeValues.put(DATE_ADDED_VIDEOS_ATTRIBUTE_NAME, (int) dateAdded.getTime() / (1000 * 60 * 60));
                    attributeNames.add(DATE_ADDED_VIDEOS_ATTRIBUTE_NAME);
                    video.setAttributeValues(attributeValues);

                }

                String attributeName = rs.getString("attribute_name");
                int attributeValue = rs.getInt("attribute_value");

                attributeNames.add(attributeName);

                video.getAttributeValues().put(attributeName, attributeValue);
                
                videos.add(video);

            }

            for (String attributeName : attributeNames) {
                VideoAttributeReverseComparator comparator = new VideoAttributeReverseComparator(attributeName);
                List<Video> orderedList = new ArrayList<>();
                orderedList.addAll(videos);
                Collections.sort(orderedList, comparator);

                OrderedVideos orderedVideos = new OrderedVideos();
                orderedVideos.setId(categoryId + "_" + attributeName);
                orderedVideos.setName(attributeName);
                orderedVideos.setChildType(EntityType.VIDEO);
                orderedVideos.setChildren(orderedList.stream().collect(Collectors.toList()));

                orderedVideosList.add(orderedVideos);
            }
            return orderedVideosList;
        } catch (SQLException ex) {
            throw new RuntimeException("Problem getting video data.", ex);
        }
    }
}
