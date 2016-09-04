package com.appify.vidstream.webservices;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.Logger;

public class DataSourceFactory {

	 private static DataSource dataSource;
	 private static Logger log=Logger.getLogger(DataSourceFactory.class.getName());
	 
	 /**
		 * @return the dataSource
		 */
		public static DataSource getDataSource() {
			if(dataSource==null){
				loadDataSource();
			}
			return dataSource;
		}

		
		public static synchronized void loadDataSource() {
			if(dataSource==null){
				log.debug("DataSource is getting initialized...");
				BasicDataSource ds=new BasicDataSource();
				Properties conf=ResourceHelper.getResource();
				ds.setUrl(conf.getProperty("url"));
				ds.setDriverClassName(conf.getProperty("driverClassName"));
				ds.setUsername(conf.getProperty("username"));
				ds.setPassword(conf.getProperty("password"));
				ds.setMaxTotal(Integer.parseInt(conf.getProperty("maxConnections")));
				ds.setMaxIdle(Integer.parseInt(conf.getProperty("maxConnections")));
				dataSource=ds;
				log.debug("DataSource successfully created with total Connections..."+ds.getMaxTotal());
			}
			
		}

		/**
		 * @param dataSource the dataSource to set
		 */
		public static void setDataSource(DataSource dataSource) {
			DataSourceFactory.dataSource = dataSource;
		}

}
