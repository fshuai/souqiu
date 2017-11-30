package com.dml.internet;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class GetConnectionTvForecast {
	public static JSONObject getCon(){
        String path = "http://202.114.18.96:8080/SouQiuNet/TvForecastServlet";
        HttpGet httpGet = new HttpGet(path);
        HttpClient httpClient = new DefaultHttpClient();
        JSONObject json = null;
        try{
        	HttpResponse httpResponse = httpClient.execute(httpGet);
        	if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
        		String str = EntityUtils.toString(httpResponse.getEntity());
        	    json = new JSONObject(str);
             }
        }catch(Exception e){
        	e.printStackTrace();
        }
        return json;
	}
}
