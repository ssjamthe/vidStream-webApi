package com.appify.vidstream.newWebApi;

/**
 * Created by swapnil on 10/01/17.
 */
public class ChildrenEntitiesResponse {

    private String[] orderAttributes;
    private CategoryResp[] categories;
    private VideoResp[] videos;


    public String[] getOrderAttributes() {
        return orderAttributes;
    }

    public void setOrderAttributes(String[] orderAttributes) {
        this.orderAttributes = orderAttributes;
    }

    public CategoryResp[] getCategories() {
        return categories;
    }

    public void setCategories(CategoryResp[] categories) {
        this.categories = categories;
    }

    public VideoResp[] getVideos() {
        return videos;
    }

    public void setVideos(VideoResp[] videos) {
        this.videos = videos;
    }
}
