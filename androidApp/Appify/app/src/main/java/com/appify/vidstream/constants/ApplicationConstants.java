package com.appify.vidstream.constants;

/**
 AppName	    : Appify VidStream App.
 Version  	    : 1.0
 Author		    : Swapnil Nandapure
 Date Created	: 12-02-2016

 Modification History:
 Name			            App_ID		    Date		            Description
 --------------------------------------------------------------------------------------------------------------------------------------------
 1. Swapnil Nandapure	    3               24-06-2016              Initial Appify App.
 2. Swapnil Nandapure	    10              06-08-2016              Initial Fitness App.
 --------------------------------------------------------------------------------------------------------------------------------------------
 **/

public interface ApplicationConstants {

    //TODO:- Application Changes
    String APP_ID = "10";                                                   // Change APP_ID = "2"    // U'll get .apk file from "Appify\app\build\outputs\apk\app_debug.apk"
    String APP_NAME = "Fitness";                                            // Change APP_NAME = ABC  // Also need to Change App Name From "app\res\values\strings.xml\app_name"
    String APP_THEME_COLOR = "#15da19";                                     // Change APP_THEME_COLOR for Alert messages Appify text color  // Also Need to change colors from "app\res\values\colors.xml"
    String APP_PACKAGE = "com.appify.vidstream.app_10";                     // This required for IMEI // Change APP_PACKAGE = com.appify.vidstream.app_10  // Also need to Change Pkg Name at line no. 4 From "app\manifests\AndroidManifest.xml\package="com.appify.vidstream.app"" and Also from Gradle Scripts\build.gradle(Module: app)\applicationId "com.appify.vidstream.app"

    //TODO:- IP ADDRESS
    String URL_IP_ADDRESS = "https://192.168.1.108:8443/vidStreamWebApi/";   //"http://103.235.104.122:8080/Appify_Server/" Need to change IP Address if required. eg: https://appifyvids.com/Appify_Server/

    //TODO:- For Ads
    String ADMOB_BANNER = "ca-app-pub-9457891612292134/4990221400";         // Change AdMob Banner Id "ca-app-pub-9457891612292134/4990221400" also need to change from "app\res\values\strings.xml\banner_admob_ad_unit_id".
    String ADMOB_INTERSTITIAL = "ca-app-pub-9457891612292134/3629409406";   // Change AdMob Interstitial Id "ca-app-pub-9457891612292134/3629409406"
    long INMOBI_BANNER = 1468657372594L;                                    // Change InMobi Banner Id          // 1468657372594L banner  : need to write at the end of Id 'L' for long value
    long INMOBI_INTERSTITIAL = 1471704245580L;                              // Change InMobi Interstitial Id    // 1471704245580L interstitial : need to write at the end of Id 'L' for long value
    String INMOBI_ACCOUNT_ID = "86e456a90dec401f9a7c9a90a3e6dfad";          // User InMobi Account Id

    //TODO:- Token ID
    String TOKEN_VALUE = "WGFtYXJpblVzZXI6WGFtYXJpblBhc3N3b2Jk";            // Change TOKEN_VALUE when you have in database

    //TODO:- Youtube Api Key collected from Google Developer Console
    String API_KEY = "AIzaSyA19p2DzkYYV0oOVglAf-OHCF-Kazwod0E";             // Make sure change where there Android API key is available at your https://console.developers.google.com

    //TODO:- Make sure do not change never ever below portion
/***************************************************************************************************************************************************************************************************/
    //TODO:- URLS
    String URL_LOADAPP = "loadApp";
    String URL_LOADCATEGORIES = "loadCategories";
    String URL_LOADCHILDCATEGORIES = "loadChildrenForCategories";
    String URL_YOUTUBEVIDEO = "videoViewed";
    String URL_FEEDBACKFORM = "feedbackForm";

    //TODO:- For Order By Section and msgs
    String LOWINTERNET = "Low internet speed!";                             // Make sure do not change never ever
    String RETRY = "Do you want to retry?";                                 // Make sure do not change never ever
    String CANTBEREACHED = "Can't be reached!";                             // Make sure do not change never ever
    String TRYAGAIN = "Do you want to try again?";                          // Make sure do not change never ever
    String AUTHENTICATION_ERROR = "Problem accessing data. Authentication failed.";
    String UPDATING_APP = "Please try updating the app from Google Play Store.";
    String LOADSORTEDVIDEOSURL_UPLOADTIME = "Upload Time";                  // Make sure do not change never ever
    String LOADSORTEDVIDEOSURL_MOSTVIEWED = "Most Viewed";                  // Make sure do not change never ever
    String LOADSORTEDVIDEOSURL_UPLOADTIMEID = "UploadTime";                 // Make sure do not change never ever
    String LOADSORTEDVIDEOSURL_MOSTVIEWEDID = "MostViewed";                 // Make sure do not change never ever

    //TODO:- Token  key
    String TOKEN_KEY = "token";                                             // Make sure do not change never ever

    //TODO:- for Entries Per Page (for CategoryScreen.getEntriesPerPageArray())
    String FIRST="10", SECOND="15", THIRD="20";                             // Only three entries are available in Array If u want to add then add more from CategoryScreen.getEntriesPerPageArray()

/***************************************************************************************************************************************************************************************************/
}
