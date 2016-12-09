package com.appify.vidstream.newWebApiTest.data;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Created by swapnil on 23/11/16.
 */
public abstract class Entity {

    private String id;
    private String name;
    private String imageId;
    private List<Entity> children;
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

    public List<Entity> getChildren() {

        return children;
    }

    public void setChildren(List<Entity> children) {

        this.children = children;
    }

    public EntityType getChildType() {
        return childType;
    }

    public void setChildType(EntityType childType) {
        this.childType = childType;
    }

    
}
