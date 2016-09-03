package com.appify.vidstream.webservices;

 /**
 ApiName	    : Appify_Server
 Author		    : Swapnil Nandapure
 Date Created	: 12-02-2016

 Modification History:
 Name			            Date		    Description
 -----------------------------------------------------------------------------------------------
 1. Swapnil Nandapure	   12-02-2016		Make Api for private server
 2.
 -----------------------------------------------------------------------------------------------
 **/

public interface ApiConstants {
	
	//URL for .properties File
		public static final String PROPERTIES_URL = "/appify/conf/vidStreamWebApi/conf.properties"; 
		
//TODO:- Make sure do not change never ever below portion
/***************************************************************************************************************************************************************************************************/
		   		
	//Image Url	
		public static final String FinalImageURL = "imageServlet?imgId=";
	
	//for loadApp.java below 4 vlaues available in property_table
		public static final String SHOW_INMOBIADWEIGHTAGE = "showInmobiAdWeightage";
		public static final String showInmobiAdWeightage_value = "0.3";	
		public static final String MIN_INTERVAL_INTERSTITIAL = "minIntervalInterstitial";
		public static final String minIntervalInterstitial_value = "50";
		public static final String SHOW_BANNER = "showBanner";
		public static final String showBanner_value = "true";
		public static final String SHOW_AD_MOVING_INSIDE = "showAdMovingInside";
		public static final String showAdMovingInside_value = "true";
	
	//for loadChildrenForCategories.java
		public static final String uploadTimeName = "Upload Time";
		public static final String uploadTimeID = "UploadTime";
		public static final String mostViewedName = "Most Viewed";
		public static final String mostViewedID = "MostViewed";
		public static final String prop_name_for_video_count = "max_videos_for_user_app";
		public static final String prop_value_for_video_count = "20";
	
	//For Additional Categories
		public static final String RECENTLY_VIEWED = "Recently Viewed";
		public static final String RECENTLY_VIEWED_ID = "-1";
		public static final String MOSTLY_VIEWED = "Mostly Viewed";
		public static final String MOSTLY_VIEWED_ID = "-2";
		public static final String mostly_viewed_cat_image_id = "mostly_viewed_cat_image_id";
		public static final String recently_viewed_cat_image_id = "recently_viewed_cat_image_id";
	
	//Video Attribute
		public static final String VIDEO_ATTRIBUTE_VIEW_COUNT_NAME = "views_count";		//available in database & check database before use
		public static final String VIDEO_ATTRIBUTE_PUBLISH_DATE_NAME = "published_date";
	
/***************************************************************************************************************************************************************************************************/		
}
