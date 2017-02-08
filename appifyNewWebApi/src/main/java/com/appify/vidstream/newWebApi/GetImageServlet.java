package com.appify.vidstream.newWebApi;

import com.appify.vidstream.newWebApi.data.*;
import com.appify.vidstream.newWebApi.data.jdbc.JDBCImageDataLoader;
import com.google.inject.Provider;
import com.google.inject.servlet.RequestParameters;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Map;

/**
 * Created by ankit on 28/11/16.
 */
@Singleton
public class GetImageServlet extends HttpServlet {

	//this can be used to load all images and cache it.
    private AppDataLoader appDataLoader;
    private Provider<Map<String, String[]>> paramsProvider;
    private JDBCImageDataLoader jdbcImageDataLoader;
    
    static final Logger getImageServletLogger = Logger.getLogger(GetImageServlet.class);
    


    @Inject
    public GetImageServlet(AppDataLoader appDataLoader,
    		JDBCImageDataLoader jdbcImageDataLoader,
                          @RequestParameters Provider<Map<String, String[]>> paramsProvider) {
        this.appDataLoader = appDataLoader;
        this.paramsProvider = paramsProvider;
        this.jdbcImageDataLoader = jdbcImageDataLoader;
    }


    @Override
    public void doGet(
            HttpServletRequest req, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("Inside DoGet Of GetImageServlet");
        RRLogs rrLogs = new RRLogs();
        long startTime = System.currentTimeMillis();
        Map<String, String[]> params = paramsProvider.get();

        String imageId = params.get("imageId")[0];
        byte[] image=jdbcImageDataLoader.getImageByImageId(imageId);
        
        response.setContentType("image/jpg");
        response.getOutputStream().flush();
		response.getOutputStream().write(image, 0, image.length);

        // Sending data to RRLogs
        String apiname = "imageServlet.java";
        String requestparam = "{" + "imgId=" + imageId+ "}";
        String responseparam = response.toString();

        long endTime = System.currentTimeMillis();
        System.out.println("EndTime : "+ endTime);
        long duration = endTime-startTime;
        String responseTime = duration + " msec.";
        rrLogs.getImageServletData(apiname, requestparam, responseparam, responseTime);

		response.getOutputStream().close();
    }    
}
