package com.appify.vidstream.newWebApiTest.util;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.appify.vidstream.newWebApiTest.PropertyHelper;
import com.appify.vidstream.newWebApiTest.PropertyNames;
import com.appify.vidstream.newWebApiTest.data.FilePropertyDataLoader;

@Singleton
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
