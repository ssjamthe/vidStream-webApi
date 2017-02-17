package com.appify.vidstream.newWebApi.data;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by swapnil on 15/01/17.
 */
public enum VideoAttribute {

    // Data names must match with names in data base if present.
    MOSTLY_VIEWED("Most Viewed", "views_count"), RECENTLY_VIEWED("View Time", "view_time"), UPLOAD_TIME("Upload Time",
            "published_date"), ADDED_TO_APP("Added To App", "added_to_app");

    private static final ImmutableMap<String, VideoAttribute> API_NAME_MAP;
    private static final ImmutableMap<String, VideoAttribute> DATA_NAME_MAP;

    static {
        VideoAttribute values[] = VideoAttribute.values();
        ImmutableMap.Builder<String, VideoAttribute> apiNameMapBuilder = ImmutableMap.builder();
        ImmutableMap.Builder<String, VideoAttribute> dataNameMapBuilder = ImmutableMap.builder();

        for (VideoAttribute value : values) {
            apiNameMapBuilder.put(value.apiName, value);
            dataNameMapBuilder.put(value.dataName, value);
        }

        API_NAME_MAP = apiNameMapBuilder.build();
        DATA_NAME_MAP = dataNameMapBuilder.build();
    }


    private final String apiName;
    private final String dataName;

    public static VideoAttribute getAttributeByApiName(String apiName) {
        VideoAttribute videoAttribute = API_NAME_MAP.get(apiName);
        if (videoAttribute == null) {
            throw new IllegalArgumentException("Unknown api name " + apiName);
        }

        return videoAttribute;
    }

    public static VideoAttribute getAttributeByDataName(String dataName) {
        VideoAttribute videoAttribute = DATA_NAME_MAP.get(dataName);
        if (videoAttribute == null) {
            throw new IllegalArgumentException("Unknown data name " + dataName);
        }

        return videoAttribute;
    }

    VideoAttribute(String apiName, String dataName) {
        this.apiName = apiName;
        this.dataName = dataName;
    }

    public String getApiName() {
        return apiName;
    }

    public String getDataName() {
        return dataName;
    }
}
