package com.appify.vidstream.newWebApiTest.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by swapnil on 04/12/16.
 */
public class SubEntityToEntityConverter {
    public List<Entity> getEntityList(List<?> subEntityList){
        List<Entity> childrenList = new ArrayList<Entity>();
        for(int i=0;i<subEntityList.size();i++){
            childrenList.add((Entity)subEntityList.get(i));
        }
        return childrenList;
    }
}
