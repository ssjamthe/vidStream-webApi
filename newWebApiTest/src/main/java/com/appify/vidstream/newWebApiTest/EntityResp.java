package com.appify.vidstream.newWebApiTest;

import com.appify.vidstream.newWebApiTest.data.Entity;
import com.appify.vidstream.newWebApiTest.data.EntityType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Created by swapnil on 04/12/16.
 */
public class EntityResp {
    private String id;
    private String name;
    private String imageId;
    private Entity[] children;
    private EntityType childType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public Entity[] getChildren() {
        return children;
    }

    public void setChildren(Entity[] children) {
        this.children = children;
    }

    public EntityType getChildType() {
        return childType;
    }

    public void setChildType(EntityType childType) {
        this.childType = childType;
    }

}
