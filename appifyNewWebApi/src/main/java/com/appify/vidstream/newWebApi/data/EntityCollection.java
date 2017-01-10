package com.appify.vidstream.newWebApi.data;

import java.util.List;

/**
 * Created by swapnil on 10/01/17.
 */
public class EntityCollection {

    private EntityType entityType;
    private List<? extends Entity> entities;

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public List<? extends Entity> getEntities() {
        return entities;
    }

    public void setEntities(List<? extends Entity> entities) {
        this.entities = entities;
    }
}
