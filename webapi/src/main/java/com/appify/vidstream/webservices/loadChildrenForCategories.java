package com.appify.vidstream.webservices;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author Swapnil Nandapure
 * @date 28-Apr-2016 
 **/
@WebServlet("/loadChildrenForCategories")
public class loadChildrenForCategories extends HttpServlet implements
		ApiConstants {
	private static final long serialVersionUID = 1L;
	static final Logger loadChildrenForCategoriesLOGGER = Logger
			.getLogger("applicationLog");
	
	public loadChildrenForCategories() {
		super();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Connection conn=null;
		RRLogs rrLogs = new RRLogs();
		String PrevVidID = null;
		String VIDEO_ATTRIBUTE_VIEW_COUNT_ID = null, VIDEO_ATTRIBUTE_PUBLISH_DATE_ID = null;
		List vidAttrList, tokenList, vidList, ViewCountSortArray, PublishDateSortArray, UnsortedVideoArray, FinalSortedVideos;
		PreparedStatement pst_videoID=null, pst_video=null, pst_subcategory=null,
				pst_childcat=null, pst_token=null;
		ResultSet rs_videoID=null, rs_video=null, rs_subcategory=null, rs_childcat=null, rs_token=null;
		JSONArray videosArray = new JSONArray();
		JSONArray childCatArray = new JSONArray();
		String Image_IP_Address = DataConnection.getImageURL();
		URL IMAGE_URL = new URL(Image_IP_Address+FinalImageURL);
		
		//GetParameters
		String getAppId = request.getParameter("appId");
		String getCatId = request.getParameter("catId");
		String getOrderAttr = request.getParameter("orderAttr");
		String get_page_no = request.getParameter("page_no");
		String get_entries_per_page = request.getParameter("entries_per_page");
		String getdeviceId = request.getParameter("deviceId");
		String tokenHeader = request.getHeader("token");
		long StartTime = System.currentTimeMillis();
	    System.out.println("StartTime : "+ StartTime);

		try {
			conn = DataConnection.getConnection();
			
			tokenList = new ArrayList();
			String keyTokenQuery = "select token from auth_token where app_id='"+ getAppId +"'";
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
			
				vidAttrList = new ArrayList();
				vidList = new ArrayList();
				PublishDateSortArray = new ArrayList();
				ViewCountSortArray = new ArrayList();
				UnsortedVideoArray = new ArrayList();
				FinalSortedVideos = new ArrayList();
				StringBuffer CategoryListBuffer = new StringBuffer();
				
				int entries_per_page = Integer.parseInt(get_entries_per_page);
				System.out.println(entries_per_page);
				int pg_no = Integer.parseInt(get_page_no);
				System.out.println(pg_no);
				int before_offset = pg_no - 1;
				System.out.println(before_offset);
				int offset_value = before_offset * entries_per_page;
				System.out.println(offset_value);
				
				String Query_VideoAttributeViewCountID = "select id from video_attribute where name='"+ VIDEO_ATTRIBUTE_VIEW_COUNT_NAME +"'";
				PreparedStatement pst_vc = conn.prepareStatement(Query_VideoAttributeViewCountID);
				ResultSet rs_vc = pst_vc.executeQuery();
				while (rs_vc.next()) {
					VIDEO_ATTRIBUTE_VIEW_COUNT_ID = rs_vc.getString(1);
					System.out.println("VIDEO_ATTRIBUTE_VIEW_COUNT_ID = "+VIDEO_ATTRIBUTE_VIEW_COUNT_ID);
				}
				
				String Query_VideoAttributePublishDateID = "select id from video_attribute where name='"+ VIDEO_ATTRIBUTE_PUBLISH_DATE_NAME +"'";
				PreparedStatement pst_pd = conn.prepareStatement(Query_VideoAttributePublishDateID);
				ResultSet rs_pd = pst_pd.executeQuery();
				while (rs_pd.next()) {
					VIDEO_ATTRIBUTE_PUBLISH_DATE_ID = rs_pd.getString(1);
					System.out.println("VIDEO_ATTRIBUTE_PUBLISH_DATE_ID = "+VIDEO_ATTRIBUTE_PUBLISH_DATE_ID);
				}
				
				String GetVidAttrValue = "select video_id from video_attribute_value where attribute_id='"
						+ VIDEO_ATTRIBUTE_VIEW_COUNT_ID + "' order by value desc";
				PreparedStatement pst_vid_attr_value = conn
						.prepareStatement(GetVidAttrValue);
				ResultSet rs_vid_attr_value = pst_vid_attr_value.executeQuery();
				while (rs_vid_attr_value.next()) {
					vidAttrList.add(rs_vid_attr_value.getString(1));
				}

				//TODO For RECENTLY VIEWED
				if (getCatId.equals(RECENTLY_VIEWED_ID)) {
					System.out.println("RECENTLY VIEWED");
					JSONObject videosObject = new JSONObject();
					
					String RecentlyViewedQuery = "select video_id from video_viewed_user where device_id='"
							+ getdeviceId
							+ "' and app_id='"
							+ getAppId
							+ "' order by video_timestamp desc LIMIT '"+entries_per_page+"' OFFSET '"+offset_value+"'";
					PreparedStatement pst_recentlyviewed = conn
							.prepareStatement(RecentlyViewedQuery);
					ResultSet rs_recentlyviewed = pst_recentlyviewed.executeQuery();
					while (rs_recentlyviewed.next()){
						String videoID = rs_recentlyviewed.getString(1);
						UnsortedVideoArray.add(videoID);
					}
					
					if(uploadTimeID.equals(getOrderAttr)){ // Sorting For Upload Time
						
						String PublishDateSortQuery = "select video_id from video_attribute_value where attribute_id='"+ VIDEO_ATTRIBUTE_PUBLISH_DATE_ID +"' order by value desc";
						PreparedStatement pst_publish_date_sort = conn.prepareStatement(PublishDateSortQuery);
						ResultSet rs_publish_date_sort = pst_publish_date_sort.executeQuery();
						while (rs_publish_date_sort.next()) {
							String RsPublishDate = rs_publish_date_sort.getString(1);
							PublishDateSortArray.add(RsPublishDate);
						}
						
						System.out.println("PublishDateSortArray = "+PublishDateSortArray);
						System.out.println("UnsortedVideoArray = "+UnsortedVideoArray);
						
						String PREV = null;
						String NEW = null;
						
						for (int i = 0; i < PublishDateSortArray.size(); i++) {
							PREV = PublishDateSortArray.get(i).toString();
							for (int j = 0; j < UnsortedVideoArray.size(); j++) {
								NEW = UnsortedVideoArray.get(j).toString();
								if(PREV.equals(NEW)){
									if(!NEW.equals(FinalSortedVideos)){
										FinalSortedVideos.add(NEW);
										NEW = PREV;
									}
								}
							}
						}
						
						System.out.println("FinalSortedVideos = "+FinalSortedVideos);
						
						for (int i = 0; i < FinalSortedVideos.size(); i++) {
							String FinalVideoID = FinalSortedVideos.get(i).toString();
							String videoQuery = "select distinct name,id from youtube_video where id='"
									+ FinalVideoID + "'";
							pst_video = conn.prepareStatement(videoQuery);
							rs_video = pst_video.executeQuery();
							while (rs_video.next()) {
								JSONObject jsonObject = new JSONObject();
								String name = rs_video.getString(1);
								String vidid = rs_video.getString(2);
								jsonObject.put("name", name);
								jsonObject.put("id", vidid);
								// here is order concept
								videosArray.add(jsonObject);
							}
							pst_video.close();
							rs_video.close();
						}
						
						pst_publish_date_sort.close();
						rs_publish_date_sort.close();
						
					}else{// Sorting For Most Viewed
						
						String ViewCountSortQuery = "select video_id from video_attribute_value where attribute_id='"+ VIDEO_ATTRIBUTE_VIEW_COUNT_ID +"' order by value desc";
						PreparedStatement pst_view_count_sort = conn.prepareStatement(ViewCountSortQuery);
						ResultSet rs_view_count_sort = pst_view_count_sort.executeQuery();
						
						while (rs_view_count_sort.next()) {
							String RsViewCount = rs_view_count_sort.getString(1);
							ViewCountSortArray.add(RsViewCount);
						}
						
						System.out.println("ViewCountSortArray = "+ViewCountSortArray);
						System.out.println("UnsortedVideoArray = "+UnsortedVideoArray);
						
						String PREV = null;
						String NEW = null;
						
						for (int i = 0; i < ViewCountSortArray.size(); i++) {
							PREV = ViewCountSortArray.get(i).toString();
							for (int j = 0; j < UnsortedVideoArray.size(); j++) {
								NEW = UnsortedVideoArray.get(j).toString();
								if(PREV.equals(NEW)){
									if(!NEW.equals(FinalSortedVideos)){
										FinalSortedVideos.add(NEW);
										NEW = PREV;
									}
								}
							}
						}
						
						System.out.println("FinalSortedVideos = "+FinalSortedVideos);
						
						for (int i = 0; i < FinalSortedVideos.size(); i++) {
							String FinalVideoID = FinalSortedVideos.get(i).toString();
							String videoQuery = "select distinct name,id from youtube_video where id='"
									+ FinalVideoID + "'";
							pst_video = conn.prepareStatement(videoQuery);
							rs_video = pst_video.executeQuery();
							while (rs_video.next()) {
								JSONObject jsonObject = new JSONObject();
								String name = rs_video.getString(1);
								String vidid = rs_video.getString(2);
								jsonObject.put("name", name);
								jsonObject.put("id", vidid);
								// here is order concept
								videosArray.add(jsonObject);
							}
							pst_video.close();
							rs_video.close();
						}
						
						pst_view_count_sort.close();
						rs_view_count_sort.close();
					}
					
					videosObject.put("videos", videosArray);

					JSONArray orderArray = new JSONArray();
					orderArray.add(mostViewedName);
					orderArray.add(uploadTimeName);
					videosObject.put("orderAttributes", orderArray);

					System.out.println("VIDEO JSON RESPONSE");
					System.out.println("----------------------------");
					System.out.println(videosObject);
					System.out.println("deviceId=" + getdeviceId);
					System.out.println("---------------------------");

					try {
						String authenticationError = "Authentication Success";
						String videoGson = new Gson().toJson(videosObject);
						response.setContentType("application/json");
						response.setCharacterEncoding("UTF-8");
						response.getWriter().write(videoGson);
						
						// Sending data to RRLogs
						String apiname = "loadChildrenForCategories.java";
						String requestparam = "{" + "appId=" + getAppId + ", catId="
								+ getCatId + ", orderAttr=" + getOrderAttr
								+ ", deviceId=" + getdeviceId + "}";
						long EndTime = System.currentTimeMillis();
						System.out.println("EndTime : "+ EndTime);
						long duration = EndTime-StartTime;
						String responseTime = duration + " msec.";
						rrLogs.getLoadCategoriesData(apiname, requestparam,
								videosObject,responseTime, authenticationError);
						
					} catch (Exception e) {
						e.printStackTrace();
						loadChildrenForCategoriesLOGGER.error("response : " + "appId = " + getAppId + ", catId = " + getCatId + ", deviceID = " + getdeviceId + ", videos = " + videosObject + ", - loadChildrenForCategories error - " + e);
					}

				}
				//TODO For MOSTLY VIEWED
				else if (getCatId.equals(MOSTLY_VIEWED_ID)) { 
					System.out.println("MOSTLY VIEWED");
					JSONObject videosObject = new JSONObject();
					
					String GetCategorizationQuery = "select distinct id from categorization where app_id='"
							+ getAppId + "' order by id asc ";
					PreparedStatement pst_catz_id = conn
							.prepareStatement(GetCategorizationQuery);
					ResultSet rs_catz_id = pst_catz_id.executeQuery();
					while (rs_catz_id.next()) {
						int CatzID = rs_catz_id.getInt(1);

						String GetCategoryQuery = "select distinct id from category where categorization_id='"
								+ CatzID + "'order by id asc ";
						PreparedStatement pst_cat_id = conn
								.prepareStatement(GetCategoryQuery);
						ResultSet rs_cat_id = pst_cat_id.executeQuery();
						while (rs_cat_id.next()) {
							int CatID = rs_cat_id.getInt(1);
							CategoryListBuffer.append("'"+CatID+"'"+",");
							System.out.println("CategoryListBuffer = "+CategoryListBuffer);
						}
						pst_cat_id.close();
						rs_cat_id.close();
					}
					
					//select video_id from youtube_video_category_mapping where category_id IN ('11','17','28') LIMIT '3' OFFSET '0'
						
					String FinalCatIDs = CategoryListBuffer.substring(0, CategoryListBuffer.length()-1);
					System.out.println("Final String = "+FinalCatIDs);
					String GetVidCatMapQuery = "select video_id from youtube_video_category_mapping where category_id IN ("+ FinalCatIDs + ") LIMIT '"+entries_per_page+"' OFFSET '"+offset_value+"'";  
					System.out.println("GetVidCatMapQuery = "+GetVidCatMapQuery);
					PreparedStatement pst_vid_cat_map = conn.prepareStatement(GetVidCatMapQuery);
					ResultSet rs_vid_cat_map = pst_vid_cat_map.executeQuery();
					while (rs_vid_cat_map.next()) {
						String NewVidId = rs_vid_cat_map.getString(1).toString();
						if (NewVidId.equals(PrevVidID)) {
							System.out.println("Previous and New Video ID is Equal.");
						} else {
							vidList.add(NewVidId);
							PrevVidID = NewVidId;
						}
					}
					pst_vid_cat_map.close();
					rs_vid_cat_map.close();

					System.out.println("vidAttrList = " + vidAttrList);
					System.out.println("vidList = " + vidList);

					for (int i = 0; i < vidAttrList.size(); i++) {
						for (int j = 0; j < vidList.size(); j++) {
							if (vidAttrList.get(i).equals(vidList.get(j))) {
								String VideoID = vidList.get(j).toString();
								UnsortedVideoArray.add(VideoID);
							}
						}
					}
					
					if(uploadTimeID.equals(getOrderAttr)){ // Sorting For Upload Time
						
						String PublishDateSortQuery = "select video_id from video_attribute_value where attribute_id='"+ VIDEO_ATTRIBUTE_PUBLISH_DATE_ID +"' order by value desc";
						PreparedStatement pst_publish_date_sort = conn.prepareStatement(PublishDateSortQuery);
						ResultSet rs_publish_date_sort = pst_publish_date_sort.executeQuery();
						while (rs_publish_date_sort.next()) {
							String RsPublishDate = rs_publish_date_sort.getString(1);
							PublishDateSortArray.add(RsPublishDate);
						}
						
											
						System.out.println("PublishDateSortArray = "+PublishDateSortArray);
						System.out.println("UnsortedVideoArray = "+UnsortedVideoArray);
						
						String PREV = null;
						String NEW = null;
						
						for (int i = 0; i < PublishDateSortArray.size(); i++) {
							PREV = PublishDateSortArray.get(i).toString();
							for (int j = 0; j < UnsortedVideoArray.size(); j++) {
								NEW = UnsortedVideoArray.get(j).toString();
								if(PREV.equals(NEW)){
									if(!NEW.equals(FinalSortedVideos)){
										FinalSortedVideos.add(NEW);
										NEW = PREV;
									}
								}
							}
						}
						
						System.out.println("FinalSortedVideos = "+FinalSortedVideos);
						
						for (int i = 0; i < FinalSortedVideos.size(); i++) {
							String FinalVideoID = FinalSortedVideos.get(i).toString();
							String videoQuery = "select distinct name,id from youtube_video where id='"
									+ FinalVideoID + "'";
							pst_video = conn.prepareStatement(videoQuery);
							rs_video = pst_video.executeQuery();
							while (rs_video.next()) {
								JSONObject jsonObject = new JSONObject();
								String name = rs_video.getString(1);
								String vidid = rs_video.getString(2);
								jsonObject.put("name", name);
								jsonObject.put("id", vidid);
								// here is order concept
								videosArray.add(jsonObject);
							}
							pst_video.close();
							rs_video.close();
						}
						
						pst_publish_date_sort.close();
						rs_publish_date_sort.close();
						
					}else{// Sorting For Most Viewed
						
						String ViewCountSortQuery = "select video_id from video_attribute_value where attribute_id='"+ VIDEO_ATTRIBUTE_VIEW_COUNT_ID +"' order by value desc";
						PreparedStatement pst_view_count_sort = conn.prepareStatement(ViewCountSortQuery);
						ResultSet rs_view_count_sort = pst_view_count_sort.executeQuery();
						
						while (rs_view_count_sort.next()) {
							String RsViewCount = rs_view_count_sort.getString(1);
							ViewCountSortArray.add(RsViewCount);
						}
						
											
						System.out.println("ViewCountSortArray = "+ViewCountSortArray);
						System.out.println("UnsortedVideoArray = "+UnsortedVideoArray);
						
						String PREV = null;
						String NEW = null;
						
						for (int i = 0; i < ViewCountSortArray.size(); i++) {
							PREV = ViewCountSortArray.get(i).toString();
							for (int j = 0; j < UnsortedVideoArray.size(); j++) {
								NEW = UnsortedVideoArray.get(j).toString();
								if(PREV.equals(NEW)){
									if(!NEW.equals(FinalSortedVideos)){
										FinalSortedVideos.add(NEW);
										NEW = PREV;
									}
								}
							}
						}
						
						System.out.println("FinalSortedVideos = "+FinalSortedVideos);
						
						for (int i = 0; i < FinalSortedVideos.size(); i++) {
							String FinalVideoID = FinalSortedVideos.get(i).toString();
							String videoQuery = "select distinct name,id from youtube_video where id='"
									+ FinalVideoID + "'";
							pst_video = conn.prepareStatement(videoQuery);
							rs_video = pst_video.executeQuery();
							while (rs_video.next()) {
								JSONObject jsonObject = new JSONObject();
								String name = rs_video.getString(1);
								String vidid = rs_video.getString(2);
								jsonObject.put("name", name);
								jsonObject.put("id", vidid);
								// here is order concept
								videosArray.add(jsonObject);
							}
							pst_video.close();
							rs_video.close();
						}
						
						pst_view_count_sort.close();
						rs_view_count_sort.close();
					}
					

					videosObject.put("videos", videosArray);

					JSONArray orderArray = new JSONArray();
					orderArray.add(mostViewedName);
					orderArray.add(uploadTimeName);
					videosObject.put("orderAttributes", orderArray);

					System.out.println("VIDEO JSON RESPONSE");
					System.out.println("----------------------------");
					System.out.println(videosObject);
					System.out.println("deviceId=" + getdeviceId);
					System.out.println("---------------------------");

					try {
						String authenticationError = "Authentication Success";
						String videoGson = new Gson().toJson(videosObject);
						response.setContentType("application/json");
						response.setCharacterEncoding("UTF-8");
						response.getWriter().write(videoGson);
						
						// Sending data to RRLogs
						String apiname = "loadChildrenForCategories.java";
						String requestparam = "{" + "appId=" + getAppId + ", catId="
								+ getCatId + ", orderAttr=" + getOrderAttr
								+ ", deviceId=" + getdeviceId + "}";
						long EndTime = System.currentTimeMillis();
						System.out.println("EndTime : "+ EndTime);
						long duration = EndTime-StartTime;
						String responseTime = duration + " msec.";
						rrLogs.getLoadCategoriesData(apiname, requestparam,
								videosObject, responseTime, authenticationError);

					} catch (Exception e) {
						e.printStackTrace();
						loadChildrenForCategoriesLOGGER.error("response : " + "appId = " + getAppId + ", catId = " + getCatId + ", deviceID = " + getdeviceId + ", videos = " + videosObject + ", - loadChildrenForCategories error - " + e);
					}

					pst_catz_id.close();
					rs_catz_id.close();

				}
				//TODO For OTHER
				else {

					String subCategoryQuery = "select child_category_id from parent_child_category_mappings where parent_category_id='"
							+ getCatId + "'";
					pst_subcategory = conn.prepareStatement(subCategoryQuery);
					rs_subcategory = pst_subcategory.executeQuery();

					if (!rs_subcategory.next()) { //Response for Videos

						JSONObject videosObject = new JSONObject();
						
						String videoIDQuery = "select video_id from youtube_video_category_mapping where category_id='"
								+ getCatId + "' ORDER BY category_id asc LIMIT '"+entries_per_page+"' OFFSET '"+offset_value+"'";
						pst_videoID = conn.prepareStatement(videoIDQuery);
						rs_videoID = pst_videoID.executeQuery();

						if (!rs_videoID.next()) { //For No Video No Child Categories are Available

							System.out.println("No Video No Child Cattegories");
							JSONObject noVideoNoCatObject = new JSONObject();
							
							//For Recently Viewed 
							String RecentViewedVideo = "select device_id from video_viewed_user where device_id='"+ getdeviceId +"'";
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
								childCatArray.add(recObject);
								
								JSONObject mostObject = new JSONObject();
								mostObject.put("id", MOSTLY_VIEWED_ID);
								mostObject.put("name", MOSTLY_VIEWED);
								mostObject.put("image", IMAGE_URL.toString() + MostlyImgID);
								childCatArray.add(mostObject);
								
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
								childCatArray.add(mostObject);
								
								pst_mostly_prop_value.close();
								rs_mostly_prop_value.close();
							}

							noVideoNoCatObject.put("categories", childCatArray);

							System.out.println("CHILD CATEGORIES JSON RESPONSE");
							System.out.println("----------------------------");
							System.out.println(noVideoNoCatObject);
							System.out.println("deviceId=" + getdeviceId);
							System.out.println("---------------------------");

							try {
								String authenticationError = "Authentication Success";
								Gson gson = new GsonBuilder().disableHtmlEscaping()
										.create();
								String videoGson = gson.toJson(noVideoNoCatObject);
								response.setContentType("application/json");
								response.setCharacterEncoding("UTF-8");
								response.getWriter().write(videoGson);
								
								// Sending data to RRLogs
								String apiname = "loadChildrenForCategories.java";
								String requestparam = "{" + "appId=" + getAppId
										+ ", catId=" + getCatId + ", orderAttr="
										+ getOrderAttr + ", deviceId=" + getdeviceId + "}";
								long EndTime = System.currentTimeMillis();
								System.out.println("EndTime : "+ EndTime);
								long duration = EndTime-StartTime;
								String responseTime = duration + " msec.";
								rrLogs.getLoadCategoriesData(apiname, requestparam,
										noVideoNoCatObject, responseTime, authenticationError);
								
							} catch (Exception e) {
								e.printStackTrace();
								loadChildrenForCategoriesLOGGER.error("response : " + "appId = " + getAppId + ", catId = " + getCatId + ", deviceID = " + getdeviceId + ", noVideoNoCat = " + noVideoNoCatObject + ", - loadChildrenForCategories error - " + e);
							}
							
						} else { // For Sorted Videos
							
							// Sorting For Upload Time
							if(uploadTimeID.equals(getOrderAttr)){
								
								String PublishDateSortQuery = "select video_id from video_attribute_value where attribute_id='"+ VIDEO_ATTRIBUTE_PUBLISH_DATE_ID +"' order by value desc";
								PreparedStatement pst_publish_date_sort = conn.prepareStatement(PublishDateSortQuery);
								ResultSet rs_publish_date_sort = pst_publish_date_sort.executeQuery();
								
								while (rs_publish_date_sort.next()) {
									String RsPublishDate = rs_publish_date_sort.getString(1);
									PublishDateSortArray.add(RsPublishDate);
								}
								
								do {
									String videoID = rs_videoID.getString(1);
									UnsortedVideoArray.add(videoID);
								}while (rs_videoID.next());
								
								System.out.println("PublishDateSortArray = "+PublishDateSortArray);
								System.out.println("UnsortedVideoArray = "+UnsortedVideoArray);
								
								String PREV = null;
								String NEW = null;
								
								for (int i = 0; i < PublishDateSortArray.size(); i++) {
									PREV = PublishDateSortArray.get(i).toString();
									for (int j = 0; j < UnsortedVideoArray.size(); j++) {
										NEW = UnsortedVideoArray.get(j).toString();
										if(PREV.equals(NEW)){
											if(!NEW.equals(FinalSortedVideos)){
												FinalSortedVideos.add(NEW);
												NEW = PREV;
											}
										}
									}
								}
								
								System.out.println("FinalSortedVideos = "+FinalSortedVideos);
								
								for (int i = 0; i < FinalSortedVideos.size(); i++) {
									String FinalVideoID = FinalSortedVideos.get(i).toString();
									String videoQuery = "select distinct name,id from youtube_video where id='"
											+ FinalVideoID + "'";
									pst_video = conn.prepareStatement(videoQuery);
									rs_video = pst_video.executeQuery();
									while (rs_video.next()) {
										JSONObject jsonObject = new JSONObject();
										String name = rs_video.getString(1);
										String vidid = rs_video.getString(2);
										jsonObject.put("name", name);
										jsonObject.put("id", vidid);
										// here is order concept
										videosArray.add(jsonObject);
									}
									pst_video.close();
									rs_video.close();
								}
								
								pst_publish_date_sort.close();
								rs_publish_date_sort.close();
								
							}else{// Sorting For Most Viewed
								
								String ViewCountSortQuery = "select video_id from video_attribute_value where attribute_id='"+ VIDEO_ATTRIBUTE_VIEW_COUNT_ID +"' order by value desc";
								PreparedStatement pst_view_count_sort = conn.prepareStatement(ViewCountSortQuery);
								ResultSet rs_view_count_sort = pst_view_count_sort.executeQuery();
								
								while (rs_view_count_sort.next()) {
									String RsViewCount = rs_view_count_sort.getString(1);
									ViewCountSortArray.add(RsViewCount);
								}
								
								do {
									String videoID = rs_videoID.getString(1);
									UnsortedVideoArray.add(videoID);
								}while (rs_videoID.next());
								
								System.out.println("ViewCountSortArray = "+ViewCountSortArray);
								System.out.println("UnsortedVideoArray = "+UnsortedVideoArray);
								
								String PREV = null;
								String NEW = null;
								
								for (int i = 0; i < ViewCountSortArray.size(); i++) {
									PREV = ViewCountSortArray.get(i).toString();
									for (int j = 0; j < UnsortedVideoArray.size(); j++) {
										NEW = UnsortedVideoArray.get(j).toString();
										if(PREV.equals(NEW)){
											if(!NEW.equals(FinalSortedVideos)){
												FinalSortedVideos.add(NEW);
												NEW = PREV;
											}
										}
									}
								}
								
								System.out.println("FinalSortedVideos = "+FinalSortedVideos);
								
								for (int i = 0; i < FinalSortedVideos.size(); i++) {
									String FinalVideoID = FinalSortedVideos.get(i).toString();
									String videoQuery = "select distinct name,id from youtube_video where id='"
											+ FinalVideoID + "'";
									pst_video = conn.prepareStatement(videoQuery);
									rs_video = pst_video.executeQuery();
									while (rs_video.next()) {
										JSONObject jsonObject = new JSONObject();
										String name = rs_video.getString(1);
										String vidid = rs_video.getString(2);
										jsonObject.put("name", name);
										jsonObject.put("id", vidid);
										// here is order concept
										videosArray.add(jsonObject);
									}
									pst_video.close();
									rs_video.close();
								}
								
								pst_view_count_sort.close();
								rs_view_count_sort.close();
							}
							
							videosObject.put("videos", videosArray);

							JSONArray orderArray = new JSONArray();
							orderArray.add(mostViewedName);
							orderArray.add(uploadTimeName);
							videosObject.put("orderAttributes", orderArray);

							System.out.println("VIDEO JSON RESPONSE");
							System.out.println("----------------------------");
							System.out.println(videosObject);
							System.out.println("deviceId=" + getdeviceId);
							System.out.println("---------------------------");

							try {
								String authenticationError = "Authentication Success";
								String videoGson = new Gson().toJson(videosObject);
								response.setContentType("application/json");
								response.setCharacterEncoding("UTF-8");
								response.getWriter().write(videoGson);
								
								// Sending data to RRLogs
								String apiname = "loadChildrenForCategories.java";
								String requestparam = "{" + "appId=" + getAppId
										+ ", catId=" + getCatId + ", orderAttr="
										+ getOrderAttr + ", deviceId=" + getdeviceId
										+ "}";
								long EndTime = System.currentTimeMillis();
								System.out.println("EndTime : "+ EndTime);
								long duration = EndTime-StartTime;
								String responseTime = duration + " msec.";
								rrLogs.getLoadCategoriesData(apiname, requestparam,
										videosObject, responseTime, authenticationError);
								
							} catch (Exception e) {
								e.printStackTrace();
								loadChildrenForCategoriesLOGGER.error("response : " + "appId = " + getAppId + ", catId = " + getCatId + ", deviceID = " + getdeviceId + ", videos = " + videosObject + ", - loadChildrenForCategories error - " + e);
							}
						}

						pst_videoID.close();
						rs_videoID.close();

					} else { // Response for Child Categories
						JSONObject childCategoriesObject = new JSONObject();

						do {
							int childcatid = rs_subcategory.getInt(1);
							String selSubCatQuery = "select distinct id,name,image from category where id = '"
									+ childcatid + "'";
							pst_childcat = conn.prepareStatement(selSubCatQuery);
							rs_childcat = pst_childcat.executeQuery();

							while (rs_childcat.next()) {
								JSONObject childcatObj = new JSONObject();
								int id = rs_childcat.getInt(1);
								String name = rs_childcat.getString(2);
								int childimage = rs_childcat.getInt(3);
								childcatObj.put("id", id);
								childcatObj.put("name", name);
								// URL catURL = new URL(loadApp.IMAGE_URL +
								// childimage);
								childcatObj.put("image", IMAGE_URL.toString()
										+ childimage);

								childCatArray.add(childcatObj);
							}
						} while (rs_subcategory.next());

						/*//For Recently Viewed 
						String RecentViewedVideo = "select device_id from video_viewed_user where device_id='"+ getdeviceId +"'";
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
							childCatArray.add(recObject);
							
							JSONObject mostObject = new JSONObject();
							mostObject.put("id", MOSTLY_VIEWED_ID);
							mostObject.put("name", MOSTLY_VIEWED);
							mostObject.put("image", IMAGE_URL.toString() + MostlyImgID);
							childCatArray.add(mostObject);
							
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
							childCatArray.add(mostObject);
							
							pst_mostly_prop_value.close();
							rs_mostly_prop_value.close();
						}*/

						childCategoriesObject.put("categories", childCatArray);

						System.out.println("CHILD CATEGORIES JSON RESPONSE");
						System.out.println("----------------------------");
						System.out.println(childCategoriesObject);
						System.out.println("deviceId=" + getdeviceId);
						System.out.println("---------------------------");

						try {
							String authenticationError = "Authentication Success";
							Gson gson = new GsonBuilder().disableHtmlEscaping()
									.create();
							String videoGson = gson.toJson(childCategoriesObject);
							response.setContentType("application/json");
							response.setCharacterEncoding("UTF-8");
							response.getWriter().write(videoGson);
							
							// Sending data to RRLogs
							String apiname = "loadChildrenForCategories.java";
							String requestparam = "{" + "appId=" + getAppId
									+ ", catId=" + getCatId + ", orderAttr="
									+ getOrderAttr + ", deviceId=" + getdeviceId + "}";
							long EndTime = System.currentTimeMillis();
							System.out.println("EndTime : "+ EndTime);
							long duration = EndTime-StartTime;
							String responseTime = duration + " msec.";
							rrLogs.getLoadCategoriesData(apiname, requestparam,
									childCategoriesObject, responseTime, authenticationError);
							
						} catch (Exception e) {
							e.printStackTrace();
							loadChildrenForCategoriesLOGGER.error("response : " + "appId = " + getAppId + ", catId = " + getCatId + ", deviceID = " + getdeviceId + ", childCategories = " + childCategoriesObject + ", - loadChildrenForCategories error - " + e);
						}

						pst_childcat.close();
						rs_childcat.close();
					}
					pst_subcategory.close();
					rs_subcategory.close();
				}

				videosArray.clear();
				childCatArray.clear();
				pst_vid_attr_value.close();
				rs_vid_attr_value.close();
				
			}else{
				loadChildrenForCategoriesLOGGER.error("Token = " + tokenHeader +" is not Available for appId = " + getAppId + ", catId = " + getCatId);
				try {
					String authenticationError = "Authentication Failed";
					JSONObject childCategoriesObject = new JSONObject();
					Gson gson = new GsonBuilder().disableHtmlEscaping().create();
					String catjson = gson.toJson(authenticationError);
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(catjson);
					
					// Sending data to RRLogs
					String apiname = "loadChildrenForCategories.java";
					String requestparam = "{" + "appId=" + getAppId
							+ ", catId=" + getCatId + ", orderAttr="
							+ getOrderAttr + ", deviceId=" + getdeviceId + "}";
					long EndTime = System.currentTimeMillis();
					System.out.println("EndTime : "+ EndTime);
					long duration = EndTime-StartTime;
					String responseTime = duration + " msec.";
					rrLogs.getLoadCategoriesData(apiname, requestparam,
							childCategoriesObject, responseTime, authenticationError);
					
				} catch (Exception e) {
					e.printStackTrace();
					loadChildrenForCategoriesLOGGER.error("Token = " + tokenHeader +" is not Available for appId = " + getAppId + ", catId = " + getCatId);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			loadChildrenForCategoriesLOGGER.error("request : " + "appId = " + getAppId + ", catId = " + getCatId + ", deviceID = " + getdeviceId + ", - loadChildrenForCategories error - " + e);
		} finally {
			try {
				if(conn !=null){
					videosArray.clear();
					childCatArray.clear();
					pst_childcat.close();
					rs_childcat.close();
					conn.close();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}

	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
