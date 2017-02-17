package com.appify.vidstream.newWebApi.data;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by swapnil on 13/01/17.
 */
public class OrderedVideosListHelper {

    public static ImmutableList<Entity> createOrderedVideosList(List<Video> videos, Collection<String> attributes) {
        ImmutableList.Builder<Entity> orderedVideosListBuilder = ImmutableList.builder();
        for (String attribute : attributes) {
            VideoAttributeReverseComparator comparator = new VideoAttributeReverseComparator(attribute);
            List<Entity> sortedList = new ArrayList<>();
            sortedList.addAll(videos);
            Collections.sort(sortedList, comparator);
            OrderedVideos orderedVideos = new OrderedVideos();
            orderedVideos.setId(attribute);
            orderedVideos.setName(attribute);
            orderedVideos.setChildren(ImmutableList.copyOf(sortedList));
            orderedVideos.setChildType(EntityType.VIDEO);

            orderedVideosListBuilder.add(orderedVideos);
        }

        return orderedVideosListBuilder.build();

    }
}
