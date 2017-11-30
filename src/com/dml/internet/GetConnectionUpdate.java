package com.dml.internet;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class GetConnectionUpdate {
	
	public static String path;
	
	public static void updateClickNum(int videotag, int id){
		switch(videotag){
		case 1:
			path = "http://202.114.18.76/AndroidUpdateClickNum.aspx?id=";
			break;
		case 2:
			path = "http://202.114.18.77:8882/AndroidUpdateClickNum.aspx?id=";;
			break;
		case 3:
			path = "http://202.114.18.77:8883/AndroidUpdateClickNum.aspx?id=";
			break;
		case 4:
			path = "http://202.114.18.77:8884/AndroidUpdateClickNum.aspx?id=";
			break;
		case 5:
			path = "http://202.114.18.77:8885/AndroidUpdateClickNum.aspx?id=";
			break;
		case 6:
			path = "http://202.114.18.96:8886/AndroidUpdateClickNum.aspx?id=";
			break;
		case 7:
			path = "http://202.114.18.96:8888/AndroidUpdateClickNum.aspx?id=";
			break;
		default:
			break;
		}
		path += id;
		new Thread(){
			@Override
			public void run(){
				try{
			        HttpGet httpGet = new HttpGet(path);
			        HttpClient httpClient = new DefaultHttpClient();
				    HttpResponse httpResponse = httpClient.execute(httpGet);
	        	if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
	        		
	        	}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}.start();
	}
}
