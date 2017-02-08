package com.appify.vidstream.newWebApi;

import com.appify.vidstream.newWebApi.data.AppDataLoader;
import com.appify.vidstream.newWebApi.data.AppInfo;
import com.google.inject.Singleton;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by swapnil on 04/12/16.
 */
@Singleton
public class ValidationFilter implements Filter
{
    private AppDataLoader appDataLoader;
    
    static final Logger filterLogger = Logger.getLogger(ValidationFilter.class);

    @Inject
    public ValidationFilter(AppDataLoader appDataLoader) {
        this.appDataLoader = appDataLoader;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        filterLogger.info("ValidationFilter is initiated");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String tokenHeader = httpRequest.getHeader("token");
        String appId = request.getParameter("appId");
        
        Map<String, AppInfo> appsInfoMap = new HashMap<String, AppInfo>();
        appsInfoMap = appDataLoader.getAppsData();
        AppInfo appInfo = appsInfoMap.get(appId);
        List<String> tokens = appInfo.getTokens();

        if(tokens.contains(tokenHeader)){
            chain.doFilter(request, response);
        }
        else{
            httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND,"Invalid Token");
            filterLogger.error("Invalid Token");
        }

    }

    @Override
    public void destroy() {

    }
}
