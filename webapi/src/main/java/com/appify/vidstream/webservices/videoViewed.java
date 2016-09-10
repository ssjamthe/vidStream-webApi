package com.appify.vidstream.webservices;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Servlet implementation class videoViewed
 */
@WebServlet("/videoViewed")
public class videoViewed extends HttpServlet implements ApiConstants{
	private static final long serialVersionUID = 1L;
	static final Logger VideoViewedLOGGER = Logger.getLogger("applicationLog");
       
    public videoViewed() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection conn=null;
		int finalCountNo, propCountNo, defaultCountNo;
		PreparedStatement pst_vid_present=null, pst_insert_new_vid=null, pst_update_time=null;
		ResultSet rs_vid_present=null;
		String allData; 
		String getAppID = request.getParameter("appId");
		String getVideoID = request.getParameter("videoId");
		String getDeviceID = request.getParameter("deviceId");
		RRLogs rrLogs = new RRLogs();
		
		try {
			conn = DataConnection.getConnection();
			
			int appid = Integer.parseInt(getAppID.toString());
			
			String VidPresentQuery = "select device_id,video_id,app_id from video_viewed_user where device_id='"+ getDeviceID +"' and video_id='"+ getVideoID +"' and app_id='"+ getAppID +"'";
			pst_vid_present = conn.prepareStatement(VidPresentQuery);
			rs_vid_present = pst_vid_present.executeQuery();
			
			if(rs_vid_present.next())
			{
				System.out.println("present..\n");
					//update video time stamp
					//insert into video_viewed_user (device_id,video_id,app_id) values ('123456789',1,1)
					//update video_viewed_user set video_timestamp='2016-06-15 14:32:27.318' where device_id='123456789' and video_id=1 and app_id=1
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
					Date cdate = new Date();
					String strDate = sdf.format(cdate);
					String updateTimeStamp = "update video_viewed_user set video_timestamp='"+ strDate +"' where device_id='"+ getDeviceID +"' and video_id='"+ getVideoID +"' and app_id="+ appid +"";
					pst_update_time = conn.prepareStatement(updateTimeStamp);
					int row = pst_update_time.executeUpdate();
					if(row>0){System.out.print("Time update of "+getDeviceID+" into the database...\n");}
					allData = "All parameters are present thats why Time is update of deviceID= "+getDeviceID+" into the database...";
					pst_update_time.close();
			}
			else{
				
				//select count (device_id) from video_viewed_user where device_id='123456789' and app_id='1' 
				//for deleting old entry
				//select video_timestamp from video_viewed_user where device_id='123456789' and app_id='1' order by video_timestamp asc limit 1
				//delete from video_viewed_user where video_timestamp='get from select video_timestamp' and device_id='123456789' and app_id='1'
				//then insert
				
				String countQuery = "select count (device_id) from video_viewed_user where device_id='"+ getDeviceID +"' and app_id='"+ appid +"'";
				PreparedStatement pst_count = conn.prepareStatement(countQuery);
				ResultSet rs_count  = pst_count.executeQuery();
				rs_count.next();
				int countNo = rs_count.getInt(1);
				System.out.println("Count = "+countNo);
				
				String CheckPropName = "select prop_name from property_table where prop_name='"+ prop_name_for_video_count +"'";
				PreparedStatement pst_CheckPropName = conn.prepareStatement(CheckPropName);
				ResultSet rs_CheckPropName = pst_CheckPropName.executeQuery();
				String AfterCheckPropName = null;
				
				if(!rs_CheckPropName.next()){
					String InsertPropNameQuery = "insert into property_table (prop_name,prop_value) values (?,?)";
					PreparedStatement pst_InsertPropName = conn.prepareStatement(InsertPropNameQuery);
					pst_InsertPropName.setString(1, prop_name_for_video_count);
					pst_InsertPropName.setString(2, prop_value_for_video_count);
					int row = pst_InsertPropName.executeUpdate();
					if(row>0){System.out.print("Inserted new prop_name into the database...");}	
					pst_InsertPropName.close();
				}
				else{
					AfterCheckPropName = rs_CheckPropName.getString(1);
					System.out.println("CheckPropName = "+AfterCheckPropName);
					
				}
				
				String CheckPropCount = "select prop_value from property_table where prop_name='"+ prop_name_for_video_count +"'";
				PreparedStatement pst_check_prop_count = conn.prepareStatement(CheckPropCount);
				ResultSet rs_check_prop_count = pst_check_prop_count.executeQuery();
				rs_check_prop_count.next();
				String prop_value = rs_check_prop_count.getString(1);
				System.out.println("check prop value from property table = "+prop_value);
				
				if(prop_value != null){
					propCountNo = Integer.parseInt(prop_value);
					finalCountNo = propCountNo;
					System.out.println("propCountNo = "+propCountNo);
				}else{
					defaultCountNo = Integer.parseInt(prop_value_for_video_count);
					finalCountNo = defaultCountNo;
					System.out.println("defaultCountNo = "+defaultCountNo);
				}
				
				System.out.println("FinalCountNo = "+finalCountNo);
				
				if(countNo==finalCountNo){
					
					String selectLastVidQuery = "select video_timestamp from video_viewed_user where device_id='"+ getDeviceID +"' and app_id='"+ appid +"' order by video_timestamp asc limit 1";
					PreparedStatement pst_last_vid = conn.prepareStatement(selectLastVidQuery);
					ResultSet rs_last_vid = pst_last_vid.executeQuery();
					rs_last_vid.next();
					String oldVidTime = rs_last_vid.getString(1);
					System.out.println("Old Video Timestamp = "+oldVidTime+"\n");
					
					String deleteVidQuery = "delete from video_viewed_user where video_timestamp='"+ oldVidTime +"' and device_id='"+ getDeviceID +"' and app_id='"+ appid +"'";
					Statement pst_delete = conn.createStatement();
					pst_delete.execute(deleteVidQuery);
					System.out.println(oldVidTime+" Deleted");
					allData = "AND Delete Video Record of deviceID= "+getDeviceID+" and appID="+getAppID+" from the database...";
					
					String Insert_New_Video = "insert into video_viewed_user (device_id,video_id,app_id) values (?,?,?)";
					pst_insert_new_vid = conn.prepareStatement(Insert_New_Video);
					pst_insert_new_vid.setString(1, getDeviceID);
					pst_insert_new_vid.setString(2, getVideoID);
					pst_insert_new_vid.setInt(3, appid);
					int row = pst_insert_new_vid.executeUpdate();
					if(row>0){System.out.print("Data saved into the database...");}
					allData = "AND Data is inserted of deviceID= "+getDeviceID+", appID="+getAppID+" and videoID="+getVideoID+" into the database...";
					pst_insert_new_vid.close();
					
					pst_last_vid.close();
					rs_last_vid.close();
					pst_delete.close();
					
				}
				else{
					String Insert_New_Video = "insert into video_viewed_user (device_id,video_id,app_id) values (?,?,?)";
					pst_insert_new_vid = conn.prepareStatement(Insert_New_Video);
					pst_insert_new_vid.setString(1, getDeviceID);
					pst_insert_new_vid.setString(2, getVideoID);
					pst_insert_new_vid.setInt(3, appid);
					int row = pst_insert_new_vid.executeUpdate();
					if(row>0){System.out.print("Data saved into the database...");}
					allData = "Data is inserted of deviceID= "+getDeviceID+", appID="+getAppID+" and videoID="+getVideoID+" into the database...";
					pst_insert_new_vid.close();
				}
				
				pst_count.close();
				pst_check_prop_count.close();
				pst_CheckPropName.close();
				rs_count.close();			
				rs_check_prop_count.close();
				rs_CheckPropName.close();
			}
			
			try {
				Gson gson = new GsonBuilder().disableHtmlEscaping().create();
				String VideoResponse = gson.toJson(allData);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(VideoResponse);
				
				// Sending data to RRLogs
				String apiname = "videoViewed.java";
				String requestparam = "{" + "appId=" + getAppID
									+ ", videoId=" + getVideoID
									+ ", deviceId=" + getDeviceID + "}";
				String responseData = "{"+ allData +"}";
				rrLogs.getVideoViewedData(apiname, requestparam, responseData);
				
			} catch (Exception e) {
				e.printStackTrace();
				VideoViewedLOGGER.error("request : " + "appId = " + getAppID + ", videoID = " + getVideoID + ", deviceID = " + getDeviceID + ", - VideoViewed error - " + e);
			}
			
			conn.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			VideoViewedLOGGER.error("request : " + "appId = " + getAppID + ", videoID = " + getVideoID + ", deviceID = " + getDeviceID + ", - VideoViewed error - " + e);
			try {
				pst_vid_present.close();
				rs_vid_present.close();
				
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}finally{
			try{
				if(conn!=null){
					conn.close();
				}
			}catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
