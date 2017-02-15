package com.appify.vidstream.newWebApi;

import com.appify.vidstream.newWebApi.data.*;
import com.appify.vidstream.newWebApi.data.jdbc.JDBCFeedbackSaver;
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
public class FeedbackFormServlet extends HttpServlet {

    private Provider<Map<String, String[]>> paramsProvider;
    private JDBCFeedbackSaver jdbcFeedbackSaver;


    @Inject
    public FeedbackFormServlet(AppDataLoader appDataLoader,
    		JDBCFeedbackSaver jdbcFeedbackSaver,
                          @RequestParameters Provider<Map<String, String[]>> paramsProvider) {
        this.paramsProvider = paramsProvider;
        this.jdbcFeedbackSaver = jdbcFeedbackSaver;
    }


    @Override
    public void doPost(
            HttpServletRequest req, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.print("Inside DoPost Of FeedbackFormServlet");
        RRLogs rrLogs = new RRLogs();

        Map<String, String[]> params = paramsProvider.get();

        String appId = params.get("appId")[0];
        String deviceId = params.get("deviceId")[0];
        String userComment = params.get("userComment")[0];

        String allData=jdbcFeedbackSaver.saveFeedback(appId, deviceId, userComment);
        
        ObjectMapper mapper = new ObjectMapper();
        String jsonResponse = mapper.writeValueAsString(allData);

        response.setContentType("application/json");
        response.getWriter().write(jsonResponse.toString());

        // Sending data to RRLogs
        String apiname = "feedbackForm.java";
        String requestparam = "{" + "appId=" + appId
                + ", deviceId=" + deviceId
                + ", user_comment=" + userComment + "}";
        String responseData = "{"+ allData +"}";
        rrLogs.getFeedbackFormData(apiname, requestparam, responseData);
    
    }

}
