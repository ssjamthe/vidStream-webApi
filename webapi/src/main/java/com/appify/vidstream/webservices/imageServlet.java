package com.appify.vidstream.webservices;

/**
 * @author Swapnil Nandapure
 * @date 21-May-2016 
 * Input = http://localhost:8080/Appify_Server/imageServlet?imgId=2
 **/

import java.net.URL;
import java.sql.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.apache.log4j.Logger;

@WebServlet("/imageServlet")
public class imageServlet extends HttpServlet implements ApiConstants{
	private static final long serialVersionUID = 1L;
	private URL IMAGE_URL;
	
	static final Logger imageServletLOGGER = Logger
			.getLogger("applicationLog");

	public imageServlet() {
		super();
		try{
			String Image_IP_Address = DataConnection.getImageURL();
			IMAGE_URL= new URL(Image_IP_Address+FinalImageURL);
		}
		catch(Exception e){}
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RRLogs rrLogs = new RRLogs();
		String getImgId = request.getParameter("imgId");
		System.out.println("getImgId: "+getImgId);
		long StartTime = System.currentTimeMillis();
	    System.out.println("StartTime : "+ StartTime);
	    Connection conn=null;
		try {
			Class.forName("org.postgresql.Driver");
			conn = DataConnection.getConnection();
			
			String Query = "select distinct contents from images where id='"
					+ getImgId + "'";
			Statement st_image = conn.createStatement();
            ResultSet rs_image = st_image.executeQuery(Query);

			rs_image.next();
			String imgLen = rs_image.getString(1);
			System.out.println(imgLen.length());
			int len = imgLen.length();
			byte[] rb = new byte[len];
			InputStream readImg = rs_image.getBinaryStream(1);
			int index = readImg.read(rb, 0, len);
			System.out.println("index" + index);
			
			response.setContentType("image/jpg");
			response.getOutputStream().flush();
			response.getOutputStream().write(rb, 0, len);
			response.getOutputStream().close();
			
			System.out.println("apiName: " + "imageServlet.java");
			System.out.println("requestParams: {" + "imgId=" + getImgId + "}");
			System.out.println("response : " + IMAGE_URL.toString() + getImgId);

			// Sending data to RRLogs
			String apiname = "imageServlet.java";
			String requestparam = "{" + "imgId=" + getImgId+ "}";
			String responseparam = IMAGE_URL.toString() + getImgId;
			long EndTime = System.currentTimeMillis();
			System.out.println("EndTime : "+ EndTime);
			long duration = EndTime-StartTime;
			String responseTime = duration + " msec.";
			rrLogs.getImageServletData(apiname, requestparam, responseparam, responseTime);

			st_image.close();
			rs_image.close();

		} catch (Exception e) {
			e.printStackTrace();
			imageServletLOGGER.error("request : " + "imgId = " + getImgId + ", - imageServlet error - " + e);
			try {
				conn.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}