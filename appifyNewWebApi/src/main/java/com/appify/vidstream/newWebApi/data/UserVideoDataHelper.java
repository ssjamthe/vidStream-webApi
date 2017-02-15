package com.appify.vidstream.newWebApi.data;

import java.util.List;

/**
 * Created by swapnil on 15/01/17.
 */
public interface UserVideoDataHelper {

    List<String> getRecentVideosForUser(String appId,String deviceId);

    void updateVideoWatchedByUser(String videoId, String deviceId, String appId);

    List<String> getVideoViewedByUsersPerApp(String appId);

}
