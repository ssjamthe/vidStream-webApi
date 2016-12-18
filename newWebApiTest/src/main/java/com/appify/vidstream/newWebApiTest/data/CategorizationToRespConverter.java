package com.appify.vidstream.newWebApiTest.data;

import com.appify.vidstream.newWebApiTest.CategorizationResp;
import com.appify.vidstream.newWebApiTest.util.WebAPIUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by swapnil on 01/12/16.
 */
public class CategorizationToRespConverter {
	
    public CategorizationResp[] getCategorizationRespArray(List<Categorization> categorizationList){

        CategorizationResp[] categorizationResps = new CategorizationResp[categorizationList.size()];
        Categorization categorization = new Categorization();
        CategoryToCategoryRespConverter categoryToCategoryRespConverter = new CategoryToCategoryRespConverter();
        VideoToVideoRespConverter videoToVideoRespConverter = new VideoToVideoRespConverter();

        for(int i=0; i<categorizationList.size();i++) {

            CategorizationResp categorizationResp = new CategorizationResp();
            categorization = categorizationList.get(i);
            categorizationResp.setId(categorization.getId());
            categorizationResp.setName(categorization.getName());
            categorizationResp.setImgURL(categorization.getImageURL());

            List<Entity> entityList = categorization.getChildren();
            if (!entityList.isEmpty()) {

                if (categorization.getChildType() == EntityType.CATEGORY) {

                    List<Category> categoryList = new ArrayList<Category>();
                    for (Entity entity : entityList) {
                        categoryList.add((Category) entity);
                    }

                    categorizationResp.setCategories(categoryToCategoryRespConverter.getCategoryRespArray(categoryList));
                } else {

                    List<Video> videoList = new ArrayList<Video>();
                    for (Entity entity : entityList) {
                        videoList.add((Video) entity);
                    }
                    categorizationResp.setVideos(videoToVideoRespConverter.getVideoRespArray(videoList));
                }
             }
             else{
                //Logger msg
            }

            categorizationResps[i] = categorizationResp;
        }

        return categorizationResps;
    }

    public CategorizationResp[] getCategorizationRespArrayWithoutChildren(List<Categorization> categorizationList){

        CategorizationResp[] categorizationResps = new CategorizationResp[categorizationList.size()];
        Categorization categorization = new Categorization();
        CategoryToCategoryRespConverter categoryToCategoryRespConverter = new CategoryToCategoryRespConverter();
        VideoToVideoRespConverter videoToVideoRespConverter = new VideoToVideoRespConverter();

        for(int i=0; i<categorizationList.size();i++) {

            CategorizationResp categorizationResp = new CategorizationResp();
            categorization = categorizationList.get(i);
            categorizationResp.setId(categorization.getId());
            categorizationResp.setName(categorization.getName());
            categorizationResp.setImgURL(categorization.getImageURL());
            categorizationResps[i] = categorizationResp;
        }

        return categorizationResps;
    }
}
