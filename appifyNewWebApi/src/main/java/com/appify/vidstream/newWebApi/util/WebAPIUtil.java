package com.appify.vidstream.newWebApi.util;

import javax.inject.Inject;

import com.appify.vidstream.newWebApi.PropertyHelper;
import com.appify.vidstream.newWebApi.PropertyNames;

public class WebAPIUtil {
	private PropertyHelper  propertyHelper;
	 
	 @Inject
	 WebAPIUtil(PropertyHelper  propertyHelper) {
	        this.propertyHelper = propertyHelper;
	    }
	 
	 public String getImageURL(String imageId){
		 
        String imageIPURL=propertyHelper.getStringProperty(PropertyNames.IMAGE_IP_ADDRESS,null);
        String imageURL=imageIPURL+"imageServlet?imageId="+imageId;
        return imageURL;
	 }

}
