package com.appify.vidstream.newWebApiTest.data;

import com.appify.vidstream.newWebApiTest.EntityResp;
import com.appify.vidstream.newWebApiTest.data.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by swapnil on 02/12/16.
 */
public class EntityToEntityRespConverter {

    public EntityResp getEntityResp (Entity entity){
        EntityResp entityResp = new EntityResp();
        entityResp.setChildType(entity.getChildType());
        List<Entity> entityList = entity.getChildren();
//      entityResp.setChildren((Entity[]) entityList.toArray());
        Entity [] entities = new Entity[entityList.size()];
        
        for(int i=0;i<entityList.size();i++){
        	Entity entityOfList = entityList.get(i);
        	entities[i] = entityOfList;
        }
        
        entityResp.setChildren(entities);
        return entityResp;
    }

}
