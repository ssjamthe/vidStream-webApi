package com.appify.vidstream.newWebApi.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Swapnil Nandapure
 * @date 31-May-2016
 **/

public class RRLogs {

	private JSONObject finalJsonObject = new JSONObject();
	static final Logger RRLOGGER = Logger.getLogger("rrLog");
	
	Date datetime = new Date();
	SimpleDateFormat dateFormatTimeStamp = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	String dateshow = dateFormatTimeStamp.format(datetime);
	
	@SuppressWarnings("unchecked")
	public void getLoadAppData(String apiname, String requestparam,
			String response, String responseTime, String authenticationMessage) {
		finalJsonObject.put("apiName", apiname);
		finalJsonObject.put("timestamp", dateshow.toString());
		finalJsonObject.put("requestParams", requestparam);
		finalJsonObject.put("response", response);
		finalJsonObject.put("responseTime", responseTime);
		finalJsonObject.put("authenticationMessage", authenticationMessage);
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		String catzationjson= gson.toJson(finalJsonObject);
		RRLOGGER.info(catzationjson);
	}
	
	@SuppressWarnings("unchecked")
	public void getLoadCategoriesData(String apiName, String request,
			JSONObject response, String responseTime, String authenticationMessage) {
		finalJsonObject.put("apiName", apiName);
		finalJsonObject.put("timestamp", dateshow.toString());
		finalJsonObject.put("requestParams", request);
		finalJsonObject.put("response", response);
		finalJsonObject.put("responseTime", responseTime);
		finalJsonObject.put("authenticationMessage", authenticationMessage);
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		String catzationjson= gson.toJson(finalJsonObject);
		RRLOGGER.info(catzationjson);
	}

	@SuppressWarnings("unchecked")
	public void getLoadChildrenForCategoriesData(String apiName,
			String request, JSONObject response, String responseTime, String authenticationMessage) {
		finalJsonObject.put("apiName", apiName);
		finalJsonObject.put("timestamp", dateshow.toString());
		finalJsonObject.put("requestParams", request);
		finalJsonObject.put("response", response);
		finalJsonObject.put("responseTime", responseTime);
		finalJsonObject.put("authenticationMessage", authenticationMessage);
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		String catzationjson= gson.toJson(finalJsonObject);
		RRLOGGER.info(catzationjson);
	}

	@SuppressWarnings("unchecked")
	public void getImageServletData(String apiName, String request,
			String response, String responseTime) {
		finalJsonObject.put("apiName", apiName);
		finalJsonObject.put("timestamp", dateshow.toString());
		finalJsonObject.put("requestParams", request);
		finalJsonObject.put("responseTime", responseTime);
		finalJsonObject.put("response", response);
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		String catzationjson= gson.toJson(finalJsonObject);
		RRLOGGER.info(catzationjson);
	}
	
	@SuppressWarnings("unchecked")
	public void getVideoViewedData(String apiName, String request,
			String response){
		finalJsonObject.put("apiName", apiName);
		finalJsonObject.put("timestamp", dateshow.toString());
		finalJsonObject.put("requestParams", request);
		finalJsonObject.put("response", response);
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		String catzationjson= gson.toJson(finalJsonObject);
		RRLOGGER.info(catzationjson);
	}

	public void getLinkClickedServlet(String apiName,String request,String response)
	{
		finalJsonObject.put("apiName", apiName);
		finalJsonObject.put("timestamp", dateshow.toString());
		finalJsonObject.put("requestParams", request);
		finalJsonObject.put("response", response);
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		String catzationjson= gson.toJson(finalJsonObject);
		RRLOGGER.info(catzationjson);
	}
	
	@SuppressWarnings("unchecked")
	public void getFeedbackFormData(String apiName, String request,
			String response){
		finalJsonObject.put("apiName", apiName);
		finalJsonObject.put("timestamp", dateshow.toString());
		finalJsonObject.put("requestParams", request);
		finalJsonObject.put("response", response);
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		String catzationjson= gson.toJson(finalJsonObject);
		RRLOGGER.info(catzationjson);
	}

}
