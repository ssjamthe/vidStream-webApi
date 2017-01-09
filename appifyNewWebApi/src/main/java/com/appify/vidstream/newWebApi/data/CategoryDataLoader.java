package com.appify.vidstream.newWebApi.data;

import java.util.List;
import java.util.Map;

public abstract class CategoryDataLoader {
	
	public abstract void startLoading();

    public abstract void stopLoading();
    
    public abstract Category getTopLevelCategory();

    public abstract List<Entity> getFirstLevelChildren(String appId);
    
    public abstract List<Entity> getChildren(String appId,String categoryId);

    public String createCategoryId(String categoryName, String categoryType) {
        return categoryType + "_" + categoryName;
    }

}
