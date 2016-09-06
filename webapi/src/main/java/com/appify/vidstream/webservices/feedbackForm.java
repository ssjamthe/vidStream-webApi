package com.appify.vidstream.webservices;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

@WebServlet("/feedbackForm")
public class feedbackForm extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	static final Logger FeedbackLOGGER = Logger.getLogger("applicationLog");
       
    public feedbackForm() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RRLogs rrLogs = new RRLogs();
		Connection conn=null;
		String allData = null; 
		String getAppId = request.getParameter("appId");
		String getDeviceId = request.getParameter("deviceId");
		String getComment = request.getParameter("user_comment");
		
		try {
			
			conn = DataConnection.getConnection();
			int appid = Integer.parseInt(getAppId.toString());
			
			String FeedbackQuery = "insert into feedback (app_id,device_id,user_comment) values (?,?,?)";
			PreparedStatement pst_feedback = conn.prepareStatement(FeedbackQuery);
			pst_feedback.setInt(1, appid);
			pst_feedback.setString(2, getDeviceId);
			pst_feedback.setString(3, getComment);
			int row = pst_feedback.executeUpdate();
			if(row>0)
			{
				System.out.print("Inserted new feedback into the database...\n");
				allData = "Data is inserted of appId= "+getAppId+", deviceId="+getDeviceId+" and user_comment="+getComment+" into the database...";
			}	
			
			// Sending data to RRLogs
						String apiname = "feedbackForm.java";
						String requestparam = "{" + "appId=" + getAppId
											+ ", deviceId=" + getDeviceId
											+ ", user_comment=" + getComment + "}";
						String responseData = "{"+ allData +"}";
						rrLogs.getFeedbackFormData(apiname, requestparam, responseData);
				
			pst_feedback.close();			
			
		} catch (Exception e) {
			e.printStackTrace();
			FeedbackLOGGER.error("feedbackForm - " + e);
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
