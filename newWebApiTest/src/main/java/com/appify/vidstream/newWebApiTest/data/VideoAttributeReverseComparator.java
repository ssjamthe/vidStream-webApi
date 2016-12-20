package com.appify.vidstream.newWebApiTest.data;

import java.util.Comparator;

/**
 * Created by swapnil on 27/11/16.
 */
public class VideoAttributeReverseComparator implements Comparator<Entity> {

    private String attributeName;

    public VideoAttributeReverseComparator(String attributeName) {
        this.attributeName = attributeName;
    }

    @Override
    public int compare(Entity o1, Entity o2) {
        Integer v1 = ((Video)o1).getAttributeValues().get(attributeName);
        Integer v2 = ((Video)o2).getAttributeValues().get(attributeName);

        v1 = v1 == null ? Integer.MIN_VALUE : v1;
        v2 = v2 == null ? Integer.MIN_VALUE : v2;
        return v2.compareTo(v1);
    }
}
