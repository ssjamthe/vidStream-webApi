/**
 * 
 */
package com.appify.vidstream.newWebApi.data.jdbc;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.sql.DataSource;

import com.appify.vidstream.newWebApi.data.Video;

/**
 * @author ankitkumar
 *
 */
public class JDBCImageDataLoader {
	
	 private DataSource dataSource;

	    @Inject
	    JDBCImageDataLoader(DataSource dataSource) {
	        this.dataSource = dataSource;
	    }
	
	
	public byte[] getImageByImageId(String imageId){
		
		 try (Connection con = dataSource.getConnection();) {

	            byte[] image=null;

	            String sql = "select distinct contents from images where id='"
						+ imageId + "'";

	            Statement stmt = con.createStatement();
	            ResultSet rs = stmt.executeQuery(sql);
	            
	            rs.next();
				String imgLen = rs.getString(1);
				System.out.println(imgLen.length());
				int len = imgLen.length();
				image = new byte[len];
				InputStream inputStream = rs.getBinaryStream(1);
				int index = inputStream.read(image, 0, len);
				System.out.println("index" + index);
	            return image;
			
		}catch(Exception ex){
			throw new RuntimeException("Problem getting image with imageId..."+imageId, ex);
		}
	}

}
