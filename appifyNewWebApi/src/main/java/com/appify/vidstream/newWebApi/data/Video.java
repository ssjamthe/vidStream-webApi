package com.appify.vidstream.newWebApi.data;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.appify.vidstream.newWebApi.util.WebAPIUtil;

/**
 * Created by swapnil on 23/11/16.
 * TODO Make collections immutable
 */
public class Video extends Entity {

    private Map<String, Integer> attributeValues;
    private Timestamp dateAdded;

    public Video() {

    }

    public Video(Video other) {
        super(other);
        this.attributeValues = new HashMap<>(other.attributeValues);
        this.dateAdded = new Timestamp(other.dateAdded.getTime());
    }

    public Map<String, Integer> getAttributeValues() {
        return attributeValues;
    }

    public void setAttributeValues(Map<String, Integer> attributeValues) {
        this.attributeValues = attributeValues;
    }

    public Timestamp getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Timestamp dateAdded) {
        this.dateAdded = dateAdded;
    }
}
