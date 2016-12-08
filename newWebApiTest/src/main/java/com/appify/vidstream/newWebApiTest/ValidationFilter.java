package com.appify.vidstream.newWebApiTest;

import com.appify.vidstream.newWebApiTest.data.AppDataLoader;
import com.appify.vidstream.newWebApiTest.data.AppInfo;
import com.google.inject.Singleton;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by swapnil on 04/12/16.
 */
@Singleton
public class ValidationFilter implements Filter
{
    private AppDataLoader appDataLoader;

    @Inject
    public ValidationFilter(AppDataLoader appDataLoader) {
        this.appDataLoader = appDataLoader;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("ValidationFilter is initiated");

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String tokenHeader = httpRequest.getHeader("token");
        String appId = request.getParameter("appId");

        Map<String, AppInfo> appsInfoMap = appDataLoader.getAppsData();
        AppInfo appInfo = appsInfoMap.get(appId);
        List<String> tokens = appInfo.getTokens();

        if(tokens.contains(tokenHeader)){
            chain.doFilter(request, response);
        }
        else{
            httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND,"Invalid Token");
        }

    }

    @Override
    public void destroy() {

    }
}
