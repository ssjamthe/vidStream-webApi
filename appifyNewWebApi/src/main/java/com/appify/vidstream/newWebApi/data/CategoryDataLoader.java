package com.appify.vidstream.newWebApi.data;

import java.util.List;
import java.util.Map;

public abstract class CategoryDataLoader {

    public abstract void startLoading();

    public abstract void stopLoading();

    public abstract Category getTopLevelCategory();

    public abstract EntityCollection getChildren(String appId, String categoryId);

    /**
     * No special characters in id allowed.
     */
    public abstract String getId();

}
