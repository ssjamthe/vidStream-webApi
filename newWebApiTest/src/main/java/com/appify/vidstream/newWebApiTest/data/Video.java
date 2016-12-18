package com.appify.vidstream.newWebApiTest.data;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.appify.vidstream.newWebApiTest.util.WebAPIUtil;

/**
 * Created by swapnil on 23/11/16.
 * TODO Make collections immutable
 */
public class Video extends Entity {

    private Map<String, Integer> attributeValues;
    private Timestamp dateAdded;

    @Override
    public String getImageURL() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setImageURL(String imageURL) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Entity> getChildren() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setChildren(List<Entity> children) {
        throw new UnsupportedOperationException();
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
