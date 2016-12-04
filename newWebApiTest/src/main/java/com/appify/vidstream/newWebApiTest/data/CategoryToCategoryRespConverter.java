package com.appify.vidstream.newWebApiTest.data;

import com.appify.vidstream.newWebApiTest.CategoryResp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by swapnil on 01/12/16.
 */
public class CategoryToCategoryRespConverter {

    public CategoryResp[] getCategoryRespArray(List<Category> categoryList){

        CategoryResp[] categoryResps = new CategoryResp[categoryList.size()];
        Category category = new Category();
        VideoToVideoRespConverter videoToVideoRespConverter = new VideoToVideoRespConverter();
        CategoryToCategoryRespConverter categoryToCategoryRespConverter = new CategoryToCategoryRespConverter();

        for(int i=0;i<categoryList.size();i++) {
            CategoryResp categoryResp = new CategoryResp();
            category = categoryList.get(i);

            List<Entity> entityList = category.getChildren();

            if (!entityList.isEmpty()){
                if (category.getChildType() == EntityType.VIDEO) {
                    List<Video> videoList = new ArrayList<Video>();

                    for (Entity entity : entityList) {
                        videoList.add((Video) entity);
                    }

                    categoryResp.setVideos(videoToVideoRespConverter.getVideoRespArray(videoList));
                } else if (category.getChildType() == EntityType.CATEGORY) {
                    List<Category> categories = new ArrayList<Category>();
                    for (Entity entity : entityList) {
                        categories.add((Category) entity);
                    }

                    categoryResp.setCategories(categoryToCategoryRespConverter.getCategoryRespArray(categoryList));
                }
        }
        else{
                //logger
            }
            categoryResp.setId(category.getId());
            categoryResp.setImg(category.getImageId());
            categoryResp.setName(category.getName());


            categoryResps[i] = categoryResp;
        }

        return categoryResps;
    }

    public CategoryResp[] getCategoryRespArrayWithoutChildren(List<Category> categoryList){

        CategoryResp[] categoryResps = new CategoryResp[categoryList.size()];
        Category category = new Category();


        for(int i=0;i<categoryList.size();i++) {
            CategoryResp categoryResp = new CategoryResp();
            category = categoryList.get(i);
            categoryResp.setId(category.getId());
            categoryResp.setImg(category.getImageId());
            categoryResp.setName(category.getName());
            categoryResps[i] = categoryResp;
        }

        return categoryResps;
    }
}
