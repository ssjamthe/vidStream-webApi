package com.appify.vidstream.newWebApiTest.data;

import java.util.List;

/**
 * Created by swapnil on 23/11/16.
 */
public class Video extends Entity {

    @Override
    public String getImageId() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setImageId(String imageId) {
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
}
