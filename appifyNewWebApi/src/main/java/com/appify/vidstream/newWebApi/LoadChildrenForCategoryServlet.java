package com.appify.vidstream.newWebApi;

import com.appify.vidstream.newWebApi.data.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
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

    private static final int DEFAULT_ENTRIES_PER_PAGE = 10;

    private Provider<Map<String, String[]>> paramsProvider;
    private List<CategoryDataLoader> categoryLoaders;
    private CategoryDataLoader defaultCategoryLoader;
    private ExploreCategoryDataLoader exploreCategoryLoader;

    public LoadChildrenForCategoryServlet(@RequestParameters Provider<Map<String, String[]>> paramsProvider,
                                          List<CategoryDataLoader> categoryLoaders, ExploreCategoryDataLoader
                                                  exploreCategoryLoader) {
        this.categoryLoaders = categoryLoaders;
        this.paramsProvider = paramsProvider;
        this.exploreCategoryLoader = exploreCategoryLoader;
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Map<String, String[]> params = paramsProvider.get();
        ChildrenEntitiesResponse response = new ChildrenEntitiesResponse();

        String appId = params.get("appId")[0];
        String catIdWithPrefix = params.get("catId")[0];
        String orderAttr = params.get("orderAttr")[0];
        String pageNoParam = params.get("page_no")[0];
        String entriesPerPageParam = params.get("entries_per_page")[0];

        CategoryPrefixHelper.CategoryIdData categoryIdData = CategoryPrefixHelper.getCategoryIdData(catIdWithPrefix);

        CategoryDataLoader categoryDataLoader = getCategoryDataLoader(categoryIdData.getPrefix());

        EntityCollection entityCollection = categoryDataLoader.getChildren(appId, categoryIdData.getCategoryId());
        if (entityCollection.getEntityType() == null) {

            response.setCategories(new CategoryResp[0]);
            return;
        }

        if (entityCollection.getEntityType() == EntityType.CATEGORY) {

            CategoryResp[] categoryResps = new CategoryResp[entityCollection.getEntities().size()];
            CategoryToCategoryRespConverter converter = new CategoryToCategoryRespConverter();

            List<? extends Entity> categories = entityCollection.getEntities();
            for (int i = 0; i < categories.size(); i++) {
                Category category = (Category) categories.get(i);
                CategoryResp categoryResp = converter.getCategoryRespFromCategoryWithoutChild(categoryDataLoader
                        .getId(), category);
                categoryResps[i] = categoryResp;
            }
            response.setCategories(categoryResps);
        } else if (entityCollection.getEntityType() == EntityType.ORDERED_VIDEOS) {
            String orderAttrData = null;
            if (orderAttr != null) {
                orderAttrData = VideoAttribute.getAttributeByApiName(orderAttr).getDataName();
            }
            String[] orderAttributesApi = new String[entityCollection.getEntities().size()];
            Entity selectedOrderedVideos = entityCollection.getEntities().get(0);
            ImmutableList<? extends Entity> currOrderedVideosList = entityCollection.getEntities();
            for (int i = 0; i < currOrderedVideosList.size(); i++) {
                Entity currOrderedVideos = currOrderedVideosList.get(i);
                if (currOrderedVideos.getName().equals(orderAttrData)) {
                    selectedOrderedVideos = currOrderedVideos;
                }
                orderAttributesApi[i] = VideoAttribute.getAttributeByDataName(currOrderedVideos.getName()).getApiName();
            }

            int pageNo = Integer.parseInt(pageNoParam);
            int entriesPerPage = entriesPerPageParam != null ? Integer.parseInt(entriesPerPageParam) :
                    DEFAULT_ENTRIES_PER_PAGE;

            int startIndex = entriesPerPage * (pageNo - 1);
            int endIndex = startIndex + entriesPerPage;
            ImmutableList<? extends Entity> videos = selectedOrderedVideos.getChildren();
            endIndex = endIndex < videos.size() ? endIndex : videos.size() - 1;
            VideoResp[] videoResps = new VideoResp[endIndex - startIndex + 1];
            VideoToVideoRespConverter converter = new VideoToVideoRespConverter();
            if (startIndex < videos.size()) {
                for (int i = startIndex; i <= endIndex; i++) {
                    videoResps[i - startIndex] = converter.getVideoRespFromVideo((Video) videos.get(i));
                }
            }
            response.setOrderAttributes(orderAttributesApi);
            response.setVideos(videoResps);
        }

        ObjectMapper mapper = new ObjectMapper();
        String jsonResponse = mapper.writeValueAsString(response);

        resp.setContentType("application/json");
        resp.getWriter().write(jsonResponse.toString());

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
