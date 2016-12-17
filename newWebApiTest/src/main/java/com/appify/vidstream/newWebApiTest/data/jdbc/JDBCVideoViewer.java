/**
 * 
 */
package com.appify.vidstream.newWebApiTest.data.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;
import javax.sql.DataSource;

import com.appify.vidstream.newWebApiTest.Constants;

/**
 * @author ankitkumar
 *
 */
public class JDBCVideoViewer {
	
	 private DataSource dataSource;

	    @Inject
	    JDBCVideoViewer(DataSource dataSource) {
	        this.dataSource = dataSource;
	    }
	
	
	public String updateVideoViewCount(String appId,String deviceId,String videoId){
		
		 try (Connection conn = dataSource.getConnection();) {

				String allData;
				int appid = Integer.parseInt(appId);
				String isVideoPresentQuery = "select device_id,video_id,app_id from video_viewed_user where device_id='"+ deviceId +"' and video_id='"+ videoId +"' and app_id='"+ appId +"'";
				PreparedStatement pst_vid_present = conn.prepareStatement(isVideoPresentQuery);
				ResultSet rs_vid_present = pst_vid_present.executeQuery();
				
				if(rs_vid_present.next())
				{
					System.out.println("present..\n");
						//update video time stamp
						//insert into video_viewed_user (device_id,video_id,app_id) values ('123456789',1,1)
						//update video_viewed_user set video_timestamp='2016-06-15 14:32:27.318' where device_id='123456789' and video_id=1 and app_id=1
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
						Date cdate = new Date();
						String strDate = sdf.format(cdate);
						String updateTimeStamp = "update video_viewed_user set video_timestamp='"+ strDate +"' where device_id='"+ deviceId +"' and video_id='"+ videoId +"' and app_id="+ appid +"";
						PreparedStatement pst_update_time = conn.prepareStatement(updateTimeStamp);
						int row = pst_update_time.executeUpdate();
						if(row>0){
							System.out.print("Time update of "+deviceId+" into the database...\n");
							}
						allData = "All parameters are present thats why Time is update of deviceID= "+deviceId+" into the database...";
				}
				else{
					
					//select count (device_id) from video_viewed_user where device_id='123456789' and app_id='1' 
					//for deleting old entry
					//select video_timestamp from video_viewed_user where device_id='123456789' and app_id='1' order by video_timestamp asc limit 1
					//delete from video_viewed_user where video_timestamp='get from select video_timestamp' and device_id='123456789' and app_id='1'
					//then insert
					
					String countQuery = "select count (device_id) from video_viewed_user where device_id='"+ deviceId +"' and app_id='"+ appid +"'";
					PreparedStatement pst_count = conn.prepareStatement(countQuery);
					ResultSet rs_count  = pst_count.executeQuery();
					rs_count.next();
					int countNo = rs_count.getInt(1);
					System.out.println("Count = "+countNo);
					
					String checkPropName = "select prop_name from property_table where prop_name='"+ Constants.PROPERTY_NAME_FOR_VIDEO_COUNT +"'";
					PreparedStatement pst_CheckPropName = conn.prepareStatement(checkPropName);
					ResultSet rs_CheckPropName = pst_CheckPropName.executeQuery();
					String afterCheckPropName = null;
					
					if(!rs_CheckPropName.next()){
						String insertPropNameQuery = "insert into property_table (prop_name,prop_value) values (?,?)";
						PreparedStatement pst_InsertPropName = conn.prepareStatement(insertPropNameQuery);
						pst_InsertPropName.setString(1, Constants.PROPERTY_NAME_FOR_VIDEO_COUNT);
						pst_InsertPropName.setString(2, Constants.PROPERTY_VALUE_FOR_VIDEO_COUNT);
						int row = pst_InsertPropName.executeUpdate();
						if(row>0){
							System.out.print("Inserted new prop_name into the database...");
							}	
					}else{
						afterCheckPropName = rs_CheckPropName.getString(1);
						System.out.println("CheckPropName = "+afterCheckPropName);
						
					}
					
					String checkPropCount = "select prop_value from property_table where prop_name='"+ Constants.PROPERTY_NAME_FOR_VIDEO_COUNT +"'";
					PreparedStatement pst_check_prop_count = conn.prepareStatement(checkPropCount);
					ResultSet rs_check_prop_count = pst_check_prop_count.executeQuery();
					rs_check_prop_count.next();
					String prop_value = rs_check_prop_count.getString(1);
					System.out.println("check prop value from property table = "+prop_value);
					int finalCountNo=0;
					if(prop_value != null){
						int propCountNo = Integer.parseInt(prop_value);
						finalCountNo = propCountNo;
						System.out.println("propCountNo = "+propCountNo);
					}else{
						int defaultCountNo = Integer.parseInt(Constants.PROPERTY_VALUE_FOR_VIDEO_COUNT);
						finalCountNo = defaultCountNo;
						System.out.println("defaultCountNo = "+defaultCountNo);
					}
					
					System.out.println("FinalCountNo = "+finalCountNo);
					
					if(countNo==finalCountNo){
						
						String selectLastVidQuery = "select video_timestamp from video_viewed_user where device_id='"+ deviceId +"' and app_id='"+ appid +"' order by video_timestamp asc limit 1";
						PreparedStatement pst_last_vid = conn.prepareStatement(selectLastVidQuery);
						ResultSet rs_last_vid = pst_last_vid.executeQuery();
						rs_last_vid.next();
						String oldVidTime = rs_last_vid.getString(1);
						System.out.println("Old Video Timestamp = "+oldVidTime+"\n");
						
						String deleteVidQuery = "delete from video_viewed_user where video_timestamp='"+ oldVidTime +"' and device_id='"+ deviceId +"' and app_id='"+ appid +"'";
						Statement pst_delete = conn.createStatement();
						pst_delete.execute(deleteVidQuery);
						System.out.println(oldVidTime+" Deleted");
						allData = "AND Delete Video Record of deviceID= "+deviceId+" and appID="+appId+" from the database...";
						
						String insert_New_Video = "insert into video_viewed_user (device_id,video_id,app_id) values (?,?,?)";
						PreparedStatement pst_insert_new_vid = conn.prepareStatement(insert_New_Video);
						pst_insert_new_vid.setString(1, deviceId);
						pst_insert_new_vid.setString(2, videoId);
						pst_insert_new_vid.setInt(3, appid);
						int row = pst_insert_new_vid.executeUpdate();
						if(row>0){
							System.out.print("Data saved into the database...");
							}
						allData = "AND Data is inserted of deviceID= "+deviceId+", appID="+appId+" and videoID="+videoId+" into the database...";
						
					}
					else{
						String Insert_New_Video = "insert into video_viewed_user (device_id,video_id,app_id) values (?,?,?)";
						PreparedStatement pst_insert_new_vid = conn.prepareStatement(Insert_New_Video);
						pst_insert_new_vid.setString(1, deviceId);
						pst_insert_new_vid.setString(2, videoId);
						pst_insert_new_vid.setInt(3, appid);
						int row = pst_insert_new_vid.executeUpdate();
						if(row>0){System.out.print("Data saved into the database...");}
						allData = "Data is inserted of deviceID= "+deviceId+", appID="+appId+" and videoID="+videoId+" into the database...";
					}
					
				}
				System.out.println(allData);
				return allData;
			
		}catch(Exception ex){
			throw new RuntimeException("Problem updating videoViewCount...", ex);
		}
	}

}
