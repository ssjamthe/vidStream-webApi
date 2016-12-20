package com.appify.vidstream.newWebApiTest.data;

import com.appify.vidstream.newWebApiTest.Constants;
import com.appify.vidstream.newWebApiTest.EntityResp;
import com.appify.vidstream.newWebApiTest.OrderAttributeResp;
import com.appify.vidstream.newWebApiTest.data.Entity;

import static com.appify.vidstream.newWebApiTest.Constants.DATE_ADDED_VIDEOS_ATTRIBUTE_NAME;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by swapnil on 02/12/16.
 * TODO : Remove pagination logic from this class.
 */
public class EntityToEntityRespConverter {
	
	public EntityResp getEntityResp(Entity entity,String orderAttributeId,Integer pageNo,Integer entitiesPerPage)
	{
		EntityResp entityResp = getEntityRespWithoutChildren(entity);
		entityResp.setChildType(entity.getChildType());
		if(entity.getChildType() != null)
		{
			if(entity.getChildType() == EntityType.ORDERED_VIDEOS)
			{
				if(entitiesPerPage == null){
					entitiesPerPage = Integer.parseInt(Constants.DEFAULT_ENTRIES_PER_PAGE) ;
				} 
				if(pageNo == null){
					pageNo = Integer.parseInt(Constants.DEFAULT_PAGE_NUMBER) ;
				}
				
				entityResp.setChildType(EntityType.VIDEO);
				EntityResp[] children = new EntityResp[entity.getChildren().size()];
				OrderAttributeResp[] orderAttributes = new OrderAttributeResp[children.length];
				int orderAttributeInd=0;
				for(Entity child : entity.getChildren())
				{
					OrderAttributeResp orderAttribute = new OrderAttributeResp();
					orderAttribute.setId(child.getId());
					orderAttribute.setName(child.getName());
					
					orderAttributes[orderAttributeInd] = orderAttribute;
					orderAttributeInd++;
					
					if(orderAttributeId == null){
						orderAttributeId = child.getId();
					}
					
					if(child.getId().equals(orderAttributeId))
					{
						List<Entity> videos = child.getChildren();
						List<EntityResp> videosRespList = new ArrayList<>(); 
						for(int i=(pageNo - 1)*entitiesPerPage;i<(pageNo - 1)*entitiesPerPage + entitiesPerPage ;i++)
						{
							if(i>=videos.size())
							{
								break;
							}
							
							EntityResp videoResp = getEntityRespWithoutChildren(videos.get(i));
							videosRespList.add(videoResp);
						}
						
						EntityResp[] videosRespArr = new EntityResp[videosRespList.size()];
						int i=0;
						for(EntityResp videoResp : videosRespList)
						{
							videosRespArr[i] = videoResp;
							i++;
						}
						
						entityResp.setOrderAttributes(orderAttributes);
						entityResp.setChildren(videosRespArr);
					}	
				}
			}
			else
			{
			EntityResp[] children = new EntityResp[entity.getChildren().size()];
			int i=0;
			for(Entity child : entity.getChildren())
			{
				EntityResp childResp = getEntityRespWithoutChildren(child);
				children[i] = childResp;
				i++;
			}
			entityResp.setChildren(children);
			}
		}
		
		return entityResp;
		
	}
	
	private EntityResp getEntityRespWithoutChildren(Entity entity)
	{
		EntityResp entityResp = new EntityResp();
		entityResp.setId(entity.getId());
		entityResp.setImageURL(entity.getImageURL());
		entityResp.setName(entity.getName());
		
		return entityResp;
	}

 

}
