package com.appify.vidstream.webservices;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ResourceHelper {
	
	private static String resourceName=ApiConstants.PROPERTIES_URL;
	private static Properties resource;
	private static Boolean startedLoad=false;
	private static Logger log=Logger.getLogger(ResourceHelper.class.getName());
	/**
	 * @return the resource
	 */
	public static Properties getResource() {
		if(resource==null){
			loadResource();
		 }
		return resource;
	}
	/**
	 * @param resource the resource to set
	 */
	public static void setResource(Properties resource) {
		ResourceHelper.resource = resource;
	}
	
	private static synchronized void loadResource(){
		if(resource==null){
			if(startedLoad){
				String errorMessage="Property Loading already in progress";
				log.error(errorMessage);
				//throw new Exception(errorMessage);
			}else{
				startedLoad=true;
				InputStream input=null;
				try{
						input=new FileInputStream(resourceName);
						if(input!=null){
							resource=new Properties();
							resource.load(input);
							log.debug("Resource with name.."+resourceName+"..loaded");
						}
					} catch (IOException ex) {
						log.error(ex.getMessage());
					} finally {
						if (input != null) {
							try {
								input.close();
							} catch (IOException ex) {
								log.error(ex.getMessage());
								ex.printStackTrace();
							}
						}
					}
				}
		}
			
	}
	/**
	 * @return the resourceName
	 */
	public static String getResourceName() {
		return resourceName;
	}
	/**
	 * @param resourceName the resourceName to set
	 */
	public static void setResourceName(String resourceName) {
		ResourceHelper.resourceName = resourceName;
	}
	 

}
