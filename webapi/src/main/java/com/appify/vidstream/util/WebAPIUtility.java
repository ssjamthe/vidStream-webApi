/**
 * 
 */
package com.appify.vidstream.util;

import java.util.Properties;

import com.appify.vidstream.webservices.ResourceHelper;

/**
 * @author ankitkumar
 *
 */
public final class WebAPIUtility {
	
	public static String getImageURL(){
    	Properties propt = ResourceHelper.getResource();
    	return propt.getProperty("Image_IP_Address");
    }

}
