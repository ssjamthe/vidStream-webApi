package com.appify.vidstream.newWebApi.data;

import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * Created by swapnil on 10/01/17.
 */
public class EntityCollection {

    private EntityType entityType;
    private ImmutableList<? extends Entity> entities;

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public ImmutableList<? extends Entity> getEntities() {
        return entities;
    }

    public void setEntities(ImmutableList<? extends Entity> entities) {
        this.entities = entities;
    }
}
