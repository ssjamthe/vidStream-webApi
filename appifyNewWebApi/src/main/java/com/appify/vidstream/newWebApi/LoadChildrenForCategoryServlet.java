package com.appify.vidstream.newWebApi;

import com.appify.vidstream.newWebApi.data.*;
import com.google.inject.Provider;
import com.google.inject.servlet.RequestParameters;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by swapnil on 10/01/17.
 */
public class LoadChildrenForCategoryServlet extends HttpServlet {

    private Provider<Map<String, String[]>> paramsProvider;
    private List<CategoryDataLoader> categoryLoaders;
    private CategoryDataLoader defaultCategoryLoader;
    private ExploreFromDBCategoryDataLoader exploreCategoryLoader;

    public LoadChildrenForCategoryServlet(@RequestParameters Provider<Map<String, String[]>> paramsProvider,
                                          List<CategoryDataLoader> categoryLoaders, ExploreFromDBCategoryDataLoader exploreCategoryLoader) {
        this.categoryLoaders = categoryLoaders;
        this.paramsProvider = paramsProvider;
        this.exploreCategoryLoader = exploreCategoryLoader;
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Map<String, String[]> params = paramsProvider.get();

        String appId = params.get("appId")[0];
        String catIdWithPrefix = params.get("catId")[0];

        CategoryPrefixHelper.CategoryIdData categoryIdData = CategoryPrefixHelper.getCategoryIdData(catIdWithPrefix);

        CategoryDataLoader categoryDataLoader = getCategoryDataLoader(categoryIdData.getPrefix());

        EntityCollection entityCollection = categoryDataLoader.getChildren(appId, categoryIdData.getCategoryId());
        if (entityCollection.getEntityType() == null) {
            ChildrenEntitiesResponse response = new ChildrenEntitiesResponse();
            response.setCategories(new CategoryResp[0]);
            return;
        }

        if (entityCollection.getEntityType() == EntityType.CATEGORY) {

        } else if (entityCollection.getEntityType() == EntityType.ORDERED_VIDEOS) {
            
        }


    }

    private CategoryDataLoader getCategoryDataLoader(String prefix) {
        for (CategoryDataLoader categoryDataLoader : categoryLoaders) {
            if (categoryDataLoader.getId().equals(prefix)) {
                return categoryDataLoader;
            }
        }
        return exploreCategoryLoader;
    }
}
