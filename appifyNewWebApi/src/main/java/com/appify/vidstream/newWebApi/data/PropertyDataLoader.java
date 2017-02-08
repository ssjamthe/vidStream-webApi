package com.appify.vidstream.newWebApi.data;

import com.appify.vidstream.newWebApi.util.AbstractScheduledServiceRobust;

import java.util.Map;

public abstract class PropertyDataLoader extends AbstractScheduledServiceRobust {
    
    public abstract Map<String, String> getProps();

}
