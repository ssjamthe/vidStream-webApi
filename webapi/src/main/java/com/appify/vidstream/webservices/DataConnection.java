/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.appify.vidstream.webservices;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DataConnection implements ApiConstants{

 private static Connection conn;
 private static String DB_DRIVER, DB_SERVER, DB_PORT, DB_NAME, DB_USER, DB_PASSWORD, IMAGE_URL;
 
 public static void readConfiguration(){
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream(PROPERTIES_URL);
			// load a properties file
			prop.load(input);
			// get the property value and print it out
			DB_DRIVER = prop.getProperty("DB_DRIVER");
			DB_SERVER = prop.getProperty("DB_SERVER");
			DB_PORT =  prop.getProperty("DB_PORT");
			DB_NAME = prop.getProperty("DB_NAME");
			DB_USER = prop.getProperty("DB_USER");
			DB_PASSWORD = prop.getProperty("DB_PASSWORD");
			System.out.println("IMAGE_URL = "+prop.getProperty("Image_IP_Address"));
	
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
 
    public static Connection getConnection() throws SQLException, ClassNotFoundException {
    	readConfiguration();
    	Class.forName("org.postgresql.Driver");
        String DB_URL = ""+DB_DRIVER+"://"+DB_SERVER+":"+DB_PORT+"/"+DB_NAME+"?user="+DB_USER+"&password="+DB_PASSWORD;		//"jdbc:postgresql://localhost:5433/appify_db?user=appify_database&password=appifyvidstream";
        System.out.println("DB_URL = "+DB_URL);
        Properties props = new Properties();
        props.setProperty("loglevel", "0");
        conn = DriverManager.getConnection(DB_URL, props);
        return conn;
    }
    
    public static String getImageURL(){
    	Properties propt = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream(PROPERTIES_URL);
			// load a properties file
			propt.load(input);
			// get the property value and print it out
			IMAGE_URL = propt.getProperty("Image_IP_Address");
			System.out.println("IMAGE_URL = "+propt.getProperty("Image_IP_Address"));
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
    	return IMAGE_URL;
    }
    
}
