package com.appify.vidstream.newWebApi;


/**
 * Created by swapnil on 29/11/16.
 */

public class CategorizationResp {

    private String id;
    private String name;
    private String img;
    private CategoryResp[] categories;

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

	public CategoryResp[] getCategories() {
        return categories;
    }

    public void setCategories(CategoryResp[] categories) {
        this.categories = categories;
    }
}
