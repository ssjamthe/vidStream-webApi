package com.appify.vidstream.newWebApiTest.util;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.appify.vidstream.newWebApiTest.PropertyNames;
import com.appify.vidstream.newWebApiTest.data.FilePropertyDataLoader;

@Singleton
public class WebAPIUtil {

	 private FilePropertyDataLoader  filePropertyDataLoader;
	 
	 @Inject
	 WebAPIUtil(FilePropertyDataLoader  filePropertyDataLoader) {
	        this.filePropertyDataLoader = filePropertyDataLoader;
	    }
	 
	 public String getImageURL(String imageId){
		 Map<String, String> props = filePropertyDataLoader.getProps();
         String imageIPURL=props.get(PropertyNames.IMAGE_IP_ADDRESS);
         String imageURL=imageIPURL+"imageServlet?imageId="+imageId;
         return imageURL;
	 }
	
}
