package com.appify.vidstream.newWebApiTest;

import com.appify.vidstream.newWebApiTest.data.Categorization;

/**
 * Created by swapnil on 29/11/16.
 */
public class TabResp {

    private String id;
    private String name;
    private String img;
    private CategorizationResp[] categorizations;
    private CategoryResp[] categories;
    private VideoResp[] videos;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public CategorizationResp[] getCategorizations() {
        return categorizations;
    }

    public void setCategorizations(CategorizationResp[] categorizations) {
        this.categorizations = categorizations;
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