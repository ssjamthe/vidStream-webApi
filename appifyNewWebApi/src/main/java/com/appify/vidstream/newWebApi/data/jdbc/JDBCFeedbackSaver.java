/**
 * 
 */
package com.appify.vidstream.newWebApi.data.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.inject.Inject;
import javax.sql.DataSource;


/**
 * @author ankitkumar
 *
 */
public class JDBCFeedbackSaver {
	
	 private DataSource dataSource;

	    @Inject
	    JDBCFeedbackSaver(DataSource dataSource) {
	        this.dataSource = dataSource;
	    }
	
	
	public String saveFeedback(String appId,String deviceId,String userComments){
		
		 try (Connection conn = dataSource.getConnection();) {
				String allData="No Data Saved";
			 	int appid = Integer.parseInt(appId.toString());
				String feedbackQuery = "insert into feedback (app_id,device_id,user_comment) values (?,?,?)";
				PreparedStatement pst_feedback = conn.prepareStatement(feedbackQuery);
				pst_feedback.setInt(1, appid);
				pst_feedback.setString(2, deviceId);
				pst_feedback.setString(3, userComments);
				int row = pst_feedback.executeUpdate();
				if(row>0)
				{
					System.out.print("Inserted new feedback into the database...\n");
					allData = "Data is inserted of appId= "+appId+", deviceId="+deviceId+" and user_comment="+userComments+" into the database...";
				}	
				
				return allData;
			
		}catch(Exception ex){
			throw new RuntimeException("Problem usaving feedback...", ex);
		}
	}

}
