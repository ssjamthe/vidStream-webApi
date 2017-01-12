package com.appify.vidstream.newWebApi.data;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.collect.ImmutableList;

/**
 * Created by swapnil on 23/11/16.
 */
public abstract class Entity {

    private String id;
    private String name;
    private String imageURL;
    private ImmutableList<Entity> children;
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


    public ImmutableList<Entity> getChildren() {

        return children;
    }

    public void setChildren(ImmutableList<Entity> children) {

        this.children = children;
    }

    public EntityType getChildType() {
        return childType;
    }

    public void setChildType(EntityType childType) {
        this.childType = childType;
    }

	/**
	 * @return the imageURL
	 */
	public String getImageURL() {
		return imageURL;
	}

	/**
	 * @param imageURL the imageURL to set
	 */
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}


}
