package com.appify.vidstream.newWebApiTest;

import com.appify.vidstream.newWebApiTest.data.*;
import com.appify.vidstream.newWebApiTest.data.jdbc.JDBCImageDataLoader;
import com.appify.vidstream.newWebApiTest.data.jdbc.JDBCVideoViewer;
import com.fasterxml.jackson.databind.ObjectMapper;
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

/**
 * Created by ankit on 28/11/16.
 */
@Singleton
public class VideoViewServlet extends HttpServlet {

    private Provider<Map<String, String[]>> paramsProvider;
    private JDBCVideoViewer jdbcVideoViewer;


    @Inject
    public VideoViewServlet(AppDataLoader appDataLoader,
    		JDBCVideoViewer jdbcVideoViewer,
                          @RequestParameters Provider<Map<String, String[]>> paramsProvider) {
        this.paramsProvider = paramsProvider;
        this.jdbcVideoViewer = jdbcVideoViewer;
    }


    @Override
    public void doGet(
            HttpServletRequest req, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.print("Inside DoGet Of VideoViewServlet");

        Map<String, String[]> params = paramsProvider.get();

        String appId = params.get("appId")[0];
        String deviceId = params.get("deviceId")[0];
        String videoId = params.get("videoId")[0];

        String allData=jdbcVideoViewer.updateVideoViewCount(appId, deviceId, videoId);
        
        ObjectMapper mapper = new ObjectMapper();
        String jsonResponse = mapper.writeValueAsString(allData);

        response.setContentType("application/json");
        response.getWriter().write(jsonResponse.toString());
    
    }

}
