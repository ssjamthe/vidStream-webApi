package com.appify.vidstream.webservices;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.appify.vidstream.util.WebAPIUtility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author Swapnil Nandapure
 * @date 26-Apr-2016 
 **/
@WebServlet("/loadApp")
public class loadApp extends HttpServlet implements ApiConstants {
	private static final long serialVersionUID = 1L;
	static final Logger loadAppLOGGER = Logger.getLogger("applicationLog");

	public loadApp() {
		super();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		int bgimg_id;
		RRLogs rrLogs = new RRLogs();
		Connection conn=null;
		String Image_IP_Address = WebAPIUtility.getImageURL();
		URL IMAGE_URL = new URL(Image_IP_Address+FinalImageURL);
		List arrList, tokenList;
		JSONArray listOfcatzation = new JSONArray();
		JSONArray categories = new JSONArray();
		JSONObject categorizationObj = new JSONObject();
		JSONObject selectedCategorizationObj = new JSONObject();
		PreparedStatement ps_categorization, pst_bgimgid,pst_selectedcategories, pst_selcatz, pst_child,pst_token;
		ResultSet rs_categorization, rs_bgimgid, rs_selectedcategories, rs_selcatz, rst_child,rs_token;
		
		//Get Parameters
		String getapp_id = request.getParameter("appId");
		String getinstallTimestamp = request.getParameter("installTimestamp");
		String getdeviceId = request.getParameter("deviceId");
		String tokenHeader = request.getHeader("token");
		
		//For RRLogs
		//System.out.println("Current Time in milliseconds = "+System.currentTimeMillis());
		long StartTime = System.currentTimeMillis();
	    System.out.println("StartTime : "+ StartTime);
 
		try {
			conn = DataConnection.getConnection();

			tokenList = new ArrayList();
			String keyTokenQuery = "select token from auth_token where app_id='"+ getapp_id +"'";
			pst_token = conn.prepareStatement(keyTokenQuery);
			rs_token = pst_token.executeQuery();
			while(rs_token.next()){
				tokenList.add(rs_token.getString(1));
			}
				
			boolean flag = false;
			for (int i = 0; i < tokenList.size(); i++) {
				if (tokenList.get(i).equals(tokenHeader)) {
					flag = true;
				}
			}
			
			if(flag == true){
				
				arrList = new ArrayList();
				String sql_child_category_list = "select distinct child_category_id from parent_child_category_mappings order by child_category_id asc";
				pst_child = conn.prepareStatement(sql_child_category_list);
				rst_child = pst_child.executeQuery();
				while (rst_child.next()) {
					arrList.add(rst_child.getInt(1));
				}

				JSONObject personalizedcatzatobj = new JSONObject();
				String query = "select id, name from categorization where app_id='"
						+ getapp_id + "' order by name asc";
				ps_categorization = conn.prepareStatement(query);
				rs_categorization = ps_categorization.executeQuery();
				
				while (rs_categorization.next()) {
					JSONObject catzatobj = new JSONObject();
					int catzation_id = rs_categorization.getInt(1);
					String catzation_name = rs_categorization.getString(2);
					if(catzation_name.equalsIgnoreCase(PERSONALIZED))
					{
						personalizedcatzatobj.put("id", catzation_id);
						personalizedcatzatobj.put("name", catzation_name);
					}
					else{
						catzatobj.put("id", catzation_id);
						catzatobj.put("name", catzation_name);
						listOfcatzation.add(catzatobj);
					}
				}
				
				listOfcatzation.add(personalizedcatzatobj);
				System.out.println("categorization List >>>> "+listOfcatzation);
				categorizationObj.put("categorizations", listOfcatzation);
				
				String showBannerQuery = "select prop_value from property_table where prop_name='"+SHOW_BANNER+"'";
				PreparedStatement pst_showbanerValue = conn.prepareStatement(showBannerQuery);
				ResultSet rs_showbanerValue = pst_showbanerValue.executeQuery();
				if(rs_showbanerValue.next()){
					String showbanerValue = rs_showbanerValue.getString(1);
						categorizationObj.put("showBanner",
								showbanerValue);
						System.out.println("DB showBanner");
				}else{
					categorizationObj.put("showBanner", showBanner_value);
					System.out.println("C showBanner");
				}

				String showAdMovingInsideQuery = "select prop_value from property_table where prop_name='"+SHOW_AD_MOVING_INSIDE+"'";
				PreparedStatement pst_showAdMovingInsideValue = conn.prepareStatement(showAdMovingInsideQuery);
				ResultSet rs_showAdMovingInsideValue = pst_showAdMovingInsideValue.executeQuery();
				if(rs_showAdMovingInsideValue.next()){
					String showAdMovingInsideValue = rs_showAdMovingInsideValue.getString(1);
						categorizationObj.put("showAdMovingInside",
								showAdMovingInsideValue);
						System.out.println("DB showAdMovingInside");
				}else{
					categorizationObj.put("showAdMovingInside", showAdMovingInside_value);
					System.out.println("C showAdMovingInside");
				}
				
				String showInmobiAdWeightageQuery = "select prop_value from property_table where prop_name='"+SHOW_INMOBIADWEIGHTAGE+"'";
				PreparedStatement pst_showInmobiAdWeightageValue = conn.prepareStatement(showInmobiAdWeightageQuery);
				ResultSet rs_showInmobiAdWeightageValue = pst_showInmobiAdWeightageValue.executeQuery();
				if(rs_showInmobiAdWeightageValue.next()){
					String showInmobiAdWeightageValue = rs_showInmobiAdWeightageValue.getString(1);
						categorizationObj.put("showInmobiAdWeightage",
								showInmobiAdWeightageValue);
						System.out.println("DB showInmobiAdWeightage");
				}else{
					categorizationObj.put("showInmobiAdWeightage",
							showInmobiAdWeightage_value);
					System.out.println("C showInmobiAdWeightage");
				}
				
				String minIntValueQuery = "select prop_value from property_table where prop_name='"+MIN_INTERVAL_INTERSTITIAL+"'";
				PreparedStatement pst_minIntValue = conn.prepareStatement(minIntValueQuery);
				ResultSet rs_minIntValue = pst_minIntValue.executeQuery();
				if(rs_minIntValue.next()){
					String minIntValue = rs_minIntValue.getString(1);
						categorizationObj.put("minIntervalInterstitial",
								minIntValue);
						System.out.println("DB minIntervalInterstitial");
				}else{
					categorizationObj.put("minIntervalInterstitial",
							minIntervalInterstitial_value);
					System.out.println("C minIntervalInterstitial");
				}

				String bgimgidQuery = "select bg_image from application where id='"
						+ getapp_id + "'";
				pst_bgimgid = conn.prepareStatement(bgimgidQuery);
				rs_bgimgid = pst_bgimgid.executeQuery();
				while(rs_bgimgid.next()){
				bgimg_id = rs_bgimgid.getInt(1);
				// URL myURL = new URL(IMAGE_URL + bgimg_id);
				categorizationObj.put("appBgImageUrl", IMAGE_URL.toString()
						+ bgimg_id);}

				//for SelectedCategorization
				String selcatzquery = "select id, name from categorization where app_id='"
						+ getapp_id + "' order by name asc";
				pst_selcatz = conn.prepareStatement(selcatzquery);
				rs_selcatz = pst_selcatz.executeQuery();
				while(rs_selcatz.next()){
					int selcatid = rs_selcatz.getInt(1);
					String selcatname = rs_selcatz.getString(2);
					selectedCategorizationObj.put("name", selcatname);
					selectedCategorizationObj.put("id", selcatid);
					String selectedCategoryQuery = "select id, name, image from category where categorization_id='"
							+ selcatid + "' order by name asc";
					pst_selectedcategories = conn
							.prepareStatement(selectedCategoryQuery);
					rs_selectedcategories = pst_selectedcategories.executeQuery();
					while (rs_selectedcategories.next()) {
						boolean match_found = true;
						int pcatId = rs_selectedcategories.getInt(1);
		
						for (int i = 0; i < arrList.size(); i++) {
							if (arrList.get(i).equals(pcatId)) {
								match_found = false;
							}
						}
						if (match_found == true) {
		
							JSONObject categoryObj = new JSONObject();
							int id = rs_selectedcategories.getInt(1);
							String name = rs_selectedcategories.getString(2);
							int imageid = rs_selectedcategories.getInt(3);
							categoryObj.put("id", id);
							categoryObj.put("name", name);
							// URL catURL = new URL(IMAGE_URL + imageid);
							categoryObj.put("image", IMAGE_URL.toString() + imageid);
							categories.add(categoryObj);
						}
					}
					pst_selectedcategories.close();
					rs_selectedcategories.close();
				}
				
				//For Recently Viewed 
				String RecentViewedVideo = "select device_id from video_viewed_user where device_id='"+ getdeviceId +"' and app_id='"+ getapp_id +"'";
				PreparedStatement pst_recent_video = conn.prepareStatement(RecentViewedVideo);
				ResultSet rs_recent_video = pst_recent_video.executeQuery();
				
				if(rs_recent_video.next()){
					
					String RecentImgPropValuesQuery = "select prop_value from property_table where prop_name='"+ recently_viewed_cat_image_id +"'";
					PreparedStatement pst_recent_prop_value = conn.prepareStatement(RecentImgPropValuesQuery);
					ResultSet rs_recent_prop_value = pst_recent_prop_value.executeQuery();
					rs_recent_prop_value.next();
					String RecentImgID = rs_recent_prop_value.getString(1);
					System.out.println("RecentImgID = "+RecentImgID);
					
					String MostlyImgPropValuesQuery = "select prop_value from property_table where prop_name='"+ mostly_viewed_cat_image_id +"'";
					PreparedStatement pst_mostly_prop_value = conn.prepareStatement(MostlyImgPropValuesQuery);
					ResultSet rs_mostly_prop_value = pst_mostly_prop_value.executeQuery();
					rs_mostly_prop_value.next();
					String MostlyImgID = rs_mostly_prop_value.getString(1);
					System.out.println("MostlyImgID = "+MostlyImgID);
					
					JSONObject recObject = new JSONObject();
					recObject.put("id", RECENTLY_VIEWED_ID);
					recObject.put("name", RECENTLY_VIEWED);
					recObject.put("image", IMAGE_URL.toString() + RecentImgID);
					categories.add(recObject);
					
					JSONObject mostObject = new JSONObject();
					mostObject.put("id", MOSTLY_VIEWED_ID);
					mostObject.put("name", MOSTLY_VIEWED);
					mostObject.put("image", IMAGE_URL.toString() + MostlyImgID);
					categories.add(mostObject);
					
					pst_recent_prop_value.close();
					rs_recent_prop_value.close();
					pst_mostly_prop_value.close();
					rs_mostly_prop_value.close();
				}
				else{
					String MostlyImgPropValuesQuery = "select prop_value from property_table where prop_name='"+ mostly_viewed_cat_image_id +"'";
					PreparedStatement pst_mostly_prop_value = conn.prepareStatement(MostlyImgPropValuesQuery);
					ResultSet rs_mostly_prop_value = pst_mostly_prop_value.executeQuery();
					rs_mostly_prop_value.next();
					String MostlyImgID = rs_mostly_prop_value.getString(1);
					System.out.println("MostlyImgID = "+MostlyImgID);
					
					JSONObject mostObject = new JSONObject();
					mostObject.put("id", MOSTLY_VIEWED_ID);
					mostObject.put("name", MOSTLY_VIEWED);
					mostObject.put("image", IMAGE_URL.toString() + MostlyImgID);
					categories.add(mostObject);
					
					pst_mostly_prop_value.close();
					rs_mostly_prop_value.close();
				}
				
				selectedCategorizationObj.put("categories", categories);

				categorizationObj.put("selectedCategorization",
						selectedCategorizationObj);

				System.out.println("CATEGORIZATION JSON RESPONSE");
				System.out.println("---------------------------");
				System.out.println(categorizationObj);
				System.out.println("installTimestamp=" + getinstallTimestamp);
				System.out.println("deviceId=" + getdeviceId);
				System.out.println("---------------------------");

				try {
					String authenticationError = "Authentication Success";
					Gson gson = new GsonBuilder().disableHtmlEscaping().create();
					String catzationjson = gson.toJson(categorizationObj);
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(catzationjson);
					
					// Sending data to RRLogs
					String apiname = "loadApp.java";
					String requestparam = "{" + "appId=" + getapp_id
										+ ", installTimestamp=" + getinstallTimestamp
										+ ", deviceId=" + getdeviceId + "}";
					long EndTime = System.currentTimeMillis();
					System.out.println("EndTime : "+ EndTime);
					long duration = EndTime-StartTime;
					String responseTime = duration + " msec.";
					rrLogs.getLoadAppData(apiname, requestparam, categorizationObj, responseTime,authenticationError);
					
				} catch (Exception e) {
					e.printStackTrace();
					loadAppLOGGER.error("response : " + "appId = " + getapp_id + ", responseParameter = " + categorizationObj + ", - loadApp error - " + e);
				}
				
				listOfcatzation.clear();
				categories.clear();
				ps_categorization.close();
				rs_categorization.close();
				pst_bgimgid.close();
				rs_bgimgid.close();
				pst_selcatz.close();
				rs_selcatz.close();
				pst_child.close();
				rst_child.close();
				
			}else{
				loadAppLOGGER.error("Token = " + tokenHeader +" is not Available for " + " appId = " + getapp_id);
				try {
					String authenticationError = "Authentication Failed";
					Gson gson = new GsonBuilder().disableHtmlEscaping().create();
					String catzationjson = gson.toJson(authenticationError);
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(catzationjson);
					
					// Sending data to RRLogs
					String apiname = "loadApp.java";
					String requestparam = "{" + "appId=" + getapp_id
										+ ", installTimestamp=" + getinstallTimestamp
										+ ", deviceId=" + getdeviceId + "}";
					long EndTime = System.currentTimeMillis();
					System.out.println("EndTime : "+ EndTime);
					long duration = EndTime-StartTime;
					String responseTime = duration + " msec.";
					rrLogs.getLoadAppData(apiname, requestparam, categorizationObj, responseTime, authenticationError);
				} catch (Exception e) {
					e.printStackTrace();
					loadAppLOGGER.error("Token = " + tokenHeader +" is not Available for " + " appId = " + getapp_id);
				}
			}
			
			pst_token.close();
			rs_token.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
			loadAppLOGGER.error("request : " + "AppID = " + getapp_id + ", - loadApp error - " + e);
		} finally {
				try{
					if(conn!=null){
						conn.close();
					}
				}catch (Exception e1) {
					e1.printStackTrace();
				}
		}

	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
