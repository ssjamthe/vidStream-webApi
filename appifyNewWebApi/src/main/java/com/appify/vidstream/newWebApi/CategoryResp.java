package com.appify.vidstream.newWebApi;

/**
 * Created by swapnil on 29/11/16.
 */
public class CategoryResp {

    private String id;
    private String name;
    private String img;
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
	public String getImg() {
		return img;
	}

	/**
	 * @param imgURL the imgURL to set
	 */
	public void setImgURL(String img) {
		this.img = img;
	}
}
