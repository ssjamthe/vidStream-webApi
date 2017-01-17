package com.appify.vidstream.newWebApi.data;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by swapnil on 17/01/17.
 */
public class OrderedVideosHelper {

    public static List<OrderedVideos> createOrderedVideosList(List<Video> videos, Set<String> attributeNames)
    {
        List<OrderedVideos> orderedVideosList = new ArrayList<>();
        for (String attributeName : attributeNames) {
            VideoAttributeReverseComparator comparator = new VideoAttributeReverseComparator(attributeName);
            List<Video> orderedList = new ArrayList<>();
            orderedList.addAll(videos);
            Collections.sort(orderedList, comparator);

            OrderedVideos orderedVideos = new OrderedVideos();
            orderedVideos.setId(attributeName);
            orderedVideos.setName(attributeName);
            orderedVideos.setChildType(EntityType.VIDEO);
            orderedVideos.setChildren(ImmutableList.copyOf(orderedList));

            orderedVideosList.add(orderedVideos);
        }
        return orderedVideosList;
    }
}
