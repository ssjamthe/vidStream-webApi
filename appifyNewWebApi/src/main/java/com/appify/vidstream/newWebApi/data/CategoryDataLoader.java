package com.appify.vidstream.newWebApi.data;

import com.appify.vidstream.newWebApi.util.AbstractScheduledServiceRobust;
import com.google.common.util.concurrent.AbstractScheduledService;

import java.util.List;
import java.util.Map;

public abstract class CategoryDataLoader extends AbstractScheduledServiceRobust{

    public abstract Category getTopLevelCategory();

    public abstract EntityCollection getChildren(String appId, String categoryId,String deviceId);

    /**
     * No special characters in id allowed.
     */
    public abstract String getId();

}
