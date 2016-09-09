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
 * @date 27-Apr-2016
 **/

@WebServlet("/loadCategories")
public class loadCategories extends HttpServlet implements ApiConstants{
	private static final long serialVersionUID = 1L;
	static final Logger loadCategoriesLOGGER = Logger.getLogger("applicationLog");

	public loadCategories() {
		super();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Connection conn=null;
		RRLogs rrLogs = new RRLogs();
		PreparedStatement ps , ps_child,pst_token;
		ResultSet rs, rs_child,rs_token;
		JSONArray catyArray = new JSONArray();
		JSONObject catyObject = new JSONObject();
		List arrList, tokenList;
		String Image_IP_Address = WebAPIUtility.getImageURL();
		URL IMAGE_URL= new URL(Image_IP_Address+FinalImageURL);
		
		//Get Parameters
		String get_app_id = request.getParameter("appId");
		String get_categorizationId = request.getParameter("categorizationId");
		String get_deviceId = request.getParameter("deviceId");
		String tokenHeader = request.getHeader("token");
		long StartTime = System.currentTimeMillis();
	    System.out.println("StartTime : "+ StartTime);
		
		try {
			conn = DataConnection.getConnection();
			
			tokenList = new ArrayList();
			String keyTokenQuery = "select token from auth_token where app_id='"+ get_app_id +"'";
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
				ps_child = conn.prepareStatement(sql_child_category_list);
				rs_child = ps_child.executeQuery();
				while(rs_child.next()){
					arrList.add(rs_child.getInt(1));
				}
				
				String query = "select id,name,image from category where categorization_id='"
						+ get_categorizationId + "'";
				ps = conn.prepareStatement(query);
				rs = ps.executeQuery();
				while (rs.next()) {
					boolean match_found = true;
					int pcatId = rs.getInt(1);
					
					for (int i = 0; i < arrList.size(); i++) {
						if(arrList.get(i).equals(pcatId)){
							match_found = false;
						}
					}
					if(match_found == true){
						JSONObject object = new JSONObject();
						int idcat = rs.getInt(1);
						String name = rs.getString(2);
						int catimg = rs.getInt(3);
						object.put("id", idcat);
						object.put("name", name);
						//URL catURL = new URL(loadApp.IMAGE_URL + catimg);
						object.put("image", IMAGE_URL.toString() + catimg);
						catyArray.add(object);
					}
				}
				
				//For Recently Viewed 
				String RecentViewedVideo = "select device_id from video_viewed_user where device_id='"+ get_deviceId +"' and app_id='"+ get_app_id +"'";
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
					catyArray.add(recObject);
					
					JSONObject mostObject = new JSONObject();
					mostObject.put("id", MOSTLY_VIEWED_ID);
					mostObject.put("name", MOSTLY_VIEWED);
					mostObject.put("image", IMAGE_URL.toString() + MostlyImgID);
					catyArray.add(mostObject);
					
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
					catyArray.add(mostObject);
					
					pst_mostly_prop_value.close();
					rs_mostly_prop_value.close();
				}
				
				catyObject.put("categories", catyArray);

				System.out.println("CATEGORIES JSON RESPONSE");
				System.out.println("---------------------------");
				System.out.println(catyObject);
				System.out.println("installTimestamp=" + get_categorizationId);
				System.out.println("deviceId=" + get_deviceId);
				System.out.println("---------------------------");
				
				try {
					String authenticationError = "Authentication Success";
					Gson gson = new GsonBuilder().disableHtmlEscaping().create();
					String catyjson = gson.toJson(catyObject);
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(catyjson);
					
					//Sending data to RRLogs
					String apiname = "loadCategories.java";
					String requestparam = "{"+"appId="+get_app_id+", categorizationId="+get_categorizationId+", deviceId="+get_deviceId+"}";
					long EndTime = System.currentTimeMillis();
					System.out.println("EndTime : "+ EndTime);
					long duration = EndTime-StartTime;
					String responseTime = duration + " msec.";
					rrLogs.getLoadCategoriesData(apiname,requestparam,catyObject,responseTime,authenticationError);

				} catch (Exception e) {
					e.printStackTrace();
					loadCategoriesLOGGER.error("response : " + "appId = " + get_app_id + ", categorizationId = " + get_categorizationId + ", responseParameter = " + catyObject + ", - loadCategories error - " + e);
				}

				catyArray.clear();
				ps.close();
				rs.close();
				ps_child.close();
				rs_child.close();
				
			}else{
				loadCategoriesLOGGER.error("Token = " + tokenHeader +" is not Available for appId = " + get_app_id + ", categorizationId = " + get_categorizationId);
				try {
					String authenticationError = "Authentication Failed";
					Gson gson = new GsonBuilder().disableHtmlEscaping().create();
					String catjson = gson.toJson(authenticationError);
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(catjson);
					
					//Sending data to RRLogs
					String apiname = "loadCategories.java";
					String requestparam = "{"+"appId="+get_app_id+", categorizationId="+get_categorizationId+", deviceId="+get_deviceId+"}";
					long EndTime = System.currentTimeMillis();
					System.out.println("EndTime : "+ EndTime);
					long duration = EndTime-StartTime;
					String responseTime = duration + " msec.";
					rrLogs.getLoadCategoriesData(apiname,requestparam,catyObject,responseTime,authenticationError);
				} catch (Exception e) {
					e.printStackTrace();
					loadCategoriesLOGGER.error("Token = " + tokenHeader +" is not Available for appId = " + get_app_id + ", categorizationId = " + get_categorizationId);
				}
			}
			catyArray.clear();
			pst_token.close();
			rs_token.close();

		} catch (Exception e) {
			e.printStackTrace();
			loadCategoriesLOGGER.error("request : " + "appId = " + get_app_id + ", categorizationId = " + get_categorizationId + ", - loadCategories error - " + e);
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
