package com.appify.vidstream.newWebApi;

import com.appify.vidstream.newWebApi.data.AppDataLoader;
import com.appify.vidstream.newWebApi.data.RRLogs;
import com.appify.vidstream.newWebApi.data.jdbc.JDBCUserVideoDataHelper;
import com.google.inject.Provider;
import com.google.inject.servlet.RequestParameters;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * Created by Kalyani on 16/01/17.
 */
@Singleton
public class VideoViewedServlet extends HttpServlet {

    static final Logger videoViewedServletLogger = Logger.getLogger(VideoViewedServlet.class);

    private AppDataLoader appDataLoader;
    private Provider<Map<String, String[]>> paramsProvider;
    private JDBCUserVideoDataHelper jdbcUserVideoDataHelper;

    @Inject
    public VideoViewedServlet(AppDataLoader appDataLoader,@RequestParameters Provider<Map<String, String[]>> paramsProvider
            ,JDBCUserVideoDataHelper jdbcUserVideoDataHelper){
        this.appDataLoader = appDataLoader;
        this.jdbcUserVideoDataHelper = jdbcUserVideoDataHelper;
        this.paramsProvider = paramsProvider;
    }

    @Override
    public void doPost(
            HttpServletRequest req, HttpServletResponse response)
            throws ServletException, IOException{
        videoViewedServletLogger.info("Inside VideoViewedServlet doGet method");
        RRLogs rrLogs = new RRLogs();

        Map<String, String[]> params = paramsProvider.get();
        String appId = params.get("appId")[0];
        String deviceId = params.get("deviceId")[0];
        String videoId = params.get("videoId")[0];

        jdbcUserVideoDataHelper.updateVideoWatchedByUser(videoId,deviceId,appId);

        // Sending data to RRLogs
        String apiname = "videoViewed.java";
        String requestparam = "{" + "appId=" + appId
                + ", videoId=" + videoId
                + ", deviceId=" + deviceId + "}";
        String responseData = "Video "+videoId+" viewed by device device id : "+deviceId+ " updated successfully" ;
        rrLogs.getVideoViewedData(apiname, requestparam, responseData);
    }
}
