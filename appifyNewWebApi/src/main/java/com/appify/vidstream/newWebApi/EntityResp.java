package com.appify.vidstream.newWebApi;

import com.appify.vidstream.newWebApi.data.Entity;
import com.appify.vidstream.newWebApi.data.EntityType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Created by swapnil on 04/12/16.
 */
public class EntityResp {
    private String id;
    private String name;
    private String imageURL;
    private EntityResp[] children;
    private EntityType childType;
   // private OrderAttributeResp[] orderAttributes;

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

    public EntityResp[] getChildren() {
        return children;
    }

    public void setChildren(EntityResp[] children) {
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

/*	public OrderAttributeResp[] getOrderAttributes() {
		return orderAttributes;
	}

	public void setOrderAttributes(OrderAttributeResp[] orderAttributes) {
		this.orderAttributes = orderAttributes;
	}*/

}
