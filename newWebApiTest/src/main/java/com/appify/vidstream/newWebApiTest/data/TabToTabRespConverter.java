package com.appify.vidstream.newWebApiTest.data;

import com.appify.vidstream.newWebApiTest.TabResp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by swapnil on 30/11/16.
 */
public class TabToTabRespConverter {
    public TabResp getTabRespFromTabWithoutChild(Tab tab){
        TabResp tabResp = new TabResp();
        tabResp.setId(tab.getId());
        tabResp.setName(tab.getName());
        tabResp.setImg(tab.getImageId());
        return tabResp;
    }

    public TabResp getTabRespFromTabWithSubChildren(Tab tab){

        TabResp tabResp = new TabResp();
        EntityType childtype = tab.getChildType();
        List<Entity> entityList = tab.getChildren();

        if(childtype == EntityType.CATEGORIZATION){
            List<Categorization> categorizationList = new ArrayList<Categorization>();
            CategorizationToRespConverter categorizationToCategorizationRespConverter = new CategorizationToRespConverter();

            for(Entity entity : entityList){
                categorizationList.add((Categorization)entity);
            }
            tabResp.setCategorizations(categorizationToCategorizationRespConverter.getCategorizationRespArray(categorizationList));
        }
        else if(childtype == EntityType.CATEGORY){
            List<Category> categoryList = new ArrayList<Category>();
            CategoryToCategoryRespConverter categoryToCategoryRespConverter = new CategoryToCategoryRespConverter();

            for(Entity entity : entityList){
                categoryList.add((Category)entity);
            }
            tabResp.setCategories(categoryToCategoryRespConverter.getCategoryRespArray(categoryList));

        }
        else{
            List<Video> videoList = new ArrayList<Video>();
            VideoToVideoRespConverter videoToVideoRespConverter = new VideoToVideoRespConverter();

            for(Entity entity : entityList){
                videoList.add((Video)entity);
            }
            tabResp.setVideos(videoToVideoRespConverter.getVideoRespArray(videoList));
        }

        tabResp.setId(tab.getId());
        tabResp.setName(tab.getName());
        tabResp.setImg(tab.getImageId());

        return tabResp;
    }

    public TabResp getTabRespFromTab(Tab tab){

        TabResp tabResp = new TabResp();
        EntityType childtype = tab.getChildType();
        List<Entity> entityList = tab.getChildren();

        if(childtype == EntityType.CATEGORIZATION){
            List<Categorization> categorizationList = new ArrayList<Categorization>();
            CategorizationToRespConverter categorizationToCategorizationRespConverter = new CategorizationToRespConverter();

            for(Entity entity : entityList){
                categorizationList.add((Categorization)entity);
            }
            tabResp.setCategorizations(categorizationToCategorizationRespConverter.getCategorizationRespArrayWithoutChildren(categorizationList));
        }
        else if(childtype == EntityType.CATEGORY){
            List<Category> categoryList = new ArrayList<Category>();
            CategoryToCategoryRespConverter categoryToCategoryRespConverter = new CategoryToCategoryRespConverter();

            for(Entity entity : entityList){
                categoryList.add((Category)entity);
            }
            tabResp.setCategories(categoryToCategoryRespConverter.getCategoryRespArrayWithoutChildren(categoryList));

        }
        else{
            List<Video> videoList = new ArrayList<Video>();
            VideoToVideoRespConverter videoToVideoRespConverter = new VideoToVideoRespConverter();

            for(Entity entity : entityList){
                videoList.add((Video)entity);
            }
            tabResp.setVideos(videoToVideoRespConverter.getVideoRespArray(videoList));
        }

        tabResp.setId(tab.getId());
        tabResp.setName(tab.getName());
        tabResp.setImg(tab.getImageId());

        return tabResp;
    }

}