package com.appify.vidstream.newWebApi;

import com.appify.vidstream.newWebApi.data.AppDataLoader;
import com.appify.vidstream.newWebApi.data.RRLogs;
import com.appify.vidstream.newWebApi.data.jdbc.JDBCUserVideoDataHelper;
import com.google.inject.Provider;
import com.google.inject.servlet.RequestParameters;
import org.apache.log4j.Logger;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Created by swapnil on 05/02/17.
 */
public class LinkClickedServlet extends HttpServlet {

    static final Logger linkClickedServletLogger = Logger.getLogger(VideoViewedServlet.class);

    private AppDataLoader appDataLoader;
    private Provider<Map<String, String[]>> paramsProvider;
    private JDBCUserVideoDataHelper jdbcUserVideoDataHelper;

    @Inject
    public LinkClickedServlet(AppDataLoader appDataLoader, @RequestParameters Provider<Map<String, String[]>>
            paramsProvider
            , JDBCUserVideoDataHelper jdbcUserVideoDataHelper) {
        this.appDataLoader = appDataLoader;
        this.jdbcUserVideoDataHelper = jdbcUserVideoDataHelper;
        this.paramsProvider = paramsProvider;
    }

    @Override
    public void doGet(
            HttpServletRequest req, HttpServletResponse response)
            throws ServletException, IOException {
        linkClickedServletLogger.info("Inside LinkClickedServlet doGet method");
        RRLogs rrLogs = new RRLogs();

        Map<String, String[]> params = paramsProvider.get();
        String appId = params.get("appId")[0];
        String deviceId = params.get("deviceId")[0];
        String linkId = params.get("linkId")[0];

        String apiname = "linkCkicked.java";
        String requestparam = "{" + "appId=" + appId
                + ", linkId=" + linkId
                + ", deviceId=" + deviceId + "}";
        String responseData = "Link "+linkId+" clicked by device device id : "+deviceId+ " logged successfully" ;
        rrLogs.getLinkClickedServlet(apiname, requestparam, responseData);

    }
}
