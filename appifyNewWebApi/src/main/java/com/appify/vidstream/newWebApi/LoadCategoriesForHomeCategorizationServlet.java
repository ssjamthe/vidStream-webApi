package com.appify.vidstream.newWebApi;

import com.appify.vidstream.newWebApi.data.Category;
import com.appify.vidstream.newWebApi.data.CategoryDataLoader;
import com.appify.vidstream.newWebApi.data.ExploreCategoryDataLoader;
import com.appify.vidstream.newWebApi.data.RRLogs;
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
import java.util.List;
import java.util.Map;

/**
 * Created by swapnil on 08/02/17.
 */
/**
 * Only Categorization possible is Home categorization.
 */
@Singleton
public class LoadCategoriesForHomeCategorizationServlet extends HttpServlet {

    private Provider<Map<String, String[]>> paramsProvider;
    private List<CategoryDataLoader> categoryLoaders;


    @Inject
    public LoadCategoriesForHomeCategorizationServlet(@RequestParameters Provider<Map<String, String[]>> paramsProvider,
    		@Annotations.CategoryDataLoaders List<CategoryDataLoader> categoryLoaders) {
        this.paramsProvider = paramsProvider;
        this.categoryLoaders = categoryLoaders;
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        long startTime = System.currentTimeMillis();
        ChildrenEntitiesResponse response = new ChildrenEntitiesResponse();
        CategoryResp[] categoryResps = new CategoryResp[categoryLoaders.size()];
        CategoryToCategoryRespConverter categoryToCategoryRespConverter = new CategoryToCategoryRespConverter();

        int i = 0;
        for (CategoryDataLoader categoryDataLoader : categoryLoaders) {
            Category category = categoryDataLoader.getTopLevelCategory();
            CategoryResp categoryResp = (CategoryResp) (categoryToCategoryRespConverter
                    .getCategoryRespFromCategoryWithoutChild(categoryDataLoader.getId(), category));
            categoryResps[i] = categoryResp;
            i++;
        }

        response.setCategories(categoryResps);
        ObjectMapper mapper = new ObjectMapper();
        String jsonResponse = mapper.writeValueAsString(response);

        Map<String, String[]> params = paramsProvider.get();
        String appId = params.get("appId")[0];
        String deviceId = params.get("deviceId")[0];

        RRLogs rrLogs = new RRLogs();
        String authenticationError = "Authentication Success";
        String apiname = "LoadCategoriesForHomeCategorizationServlet.java";
        String requestparam = "{" + "appId=" + appId
                + ", deviceId=" + deviceId + "}";
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        String responseTime = duration + " msec.";
        rrLogs.getLoadAppData(apiname, requestparam, jsonResponse, responseTime, authenticationError);

        resp.setContentType("application/json");
        resp.getWriter().write(jsonResponse.toString());

    }
}
