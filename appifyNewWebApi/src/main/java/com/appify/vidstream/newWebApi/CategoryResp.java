package com.appify.vidstream.newWebApi;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by swapnil on 29/11/16.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryResp {

    private String id;
    private String name;
    private String image;
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

	/**
	 * @return the imgURL
	 */
	public String getImage() {
		return image;
	}

	/**
	 * @param imgURL the imgURL to set
	 */
	public void setImgURL(String image) {
		this.image = image;
	}
}
