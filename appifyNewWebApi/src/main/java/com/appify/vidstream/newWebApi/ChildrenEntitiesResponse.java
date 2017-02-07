package com.appify.vidstream.newWebApi;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by swapnil on 10/01/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChildrenEntitiesResponse {

    private String[] orderAttributes;
    private CategoryResp[] categories;
    private VideoResp[] videos;
    private LinkResp[] appifyLinks;


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

    public LinkResp[] getAppifyLinks() {
        return appifyLinks;
    }

    public void setAppifyLinks(LinkResp[] appifyLinks) {
        this.appifyLinks = appifyLinks;
    }

    public void setVideos(VideoResp[] videos) {
        this.videos = videos;
    }


}
