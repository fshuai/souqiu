package com.dml.internet;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dml.bean.VideoBean;


public class GetConnectionSearch {
	
	private static String path1 = null;
	private static String baseSoccerUrl="http://202.114.18.68:8803/api/soccer/image/";
	private static String baseSnookerUrl="http://202.114.18.68:8803/api/snooker/image/";
	
	public static List<VideoBean> getCon(int mode, int pageid, String key, int query1, int query2, int query3){
        JSONArray json = null;
        List<VideoBean> data = new ArrayList<VideoBean>();
        if(query1 == 1){   //����
        	path1 = "http://202.114.18.68:8805/api/soccer/_search?q=videoname:" + key;
        
        }
        if(query1 == 2){   //����
        	path1 = "http://202.114.18.77:8882/AndroidSearch.aspx?mode=" + mode + "&key=" + key + "&pageid=" + pageid + "&orderby=" + query2 + "&lengthtype=" + query3;
        }
        if(query1 == 3){   //����
        	path1 = "http://202.114.18.77:8883/AndroidSearch.aspx?mode=" + mode + "&key=" + key + "&pageid=" + pageid + "&orderby=" + query2 + "&lengthtype=" + query3;
        }
        if(query1 == 4){   //ƹ����
        	path1 = "http://202.114.18.77:8884/AndroidSearch.aspx?mode=" + mode + "&key=" + key + "&pageid=" + pageid + "&orderby=" + query2 + "&lengthtype=" + query3;
        }
        if(query1 == 5){   //��ë��
        	path1 = "http://202.114.18.77:8885/AndroidSearch.aspx?mode=" + mode + "&key=" + key + "&pageid=" + pageid + "&orderby=" + query2 + "&lengthtype=" + query3;
        }
        if(query1 == 6){   //����
        	path1 = "http://202.114.18.96:8886/AndroidSearch.aspx?mode=" + mode + "&key=" + key + "&pageid=" + pageid + "&orderby=" + query2 + "&lengthtype=" + query3;
        }
        if(query1 == 7){   //̨��
        	path1 = "http://202.114.18.68:8805/api/snooker/_search?q=videoname:" + key;
        }
        try{
        	HttpGet httpGet = new HttpGet(path1);
	        HttpClient httpClient = new DefaultHttpClient();
        	HttpResponse httpResponse = httpClient.execute(httpGet);
        	if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
        		String str = EntityUtils.toString(httpResponse.getEntity());
        	    json = new JSONArray(str);
             }
        }catch(Exception e){
        	e.printStackTrace();
        }
    	for(int i = pageid*10; i < json.length() && i<pageid*10+10; i++){
			VideoBean video = new VideoBean(); 
			try {
				video.setId(getVideoId(json.getString(i)));
				video.setImageUrl(getImage(json.getString(i),query1));
				video.setTitle(getVideoTitle(json.getString(i))); 
				video.setClickNum(getClickNum(json.getString(i)));
				video.setLength(getDuration(json.getString(i)));
				video.setOrigin(getOrigin(json.getString(i)));
				video.setVideoUrl(getVideoUrl(json.getString(i)));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    data.add(video);
		}
        return data;
	}
	
	//解析返回的数据
	private static String getVideoId(String str){
		String[] fields=str.split(",");
		String tmp=fields[6];
		return tmp.substring(1, tmp.length()-1);
	}
	
	private static String getVideoTitle(String str){
		String[] fields=str.split(",");
		String tmp=fields[0];
		return tmp.substring(2, tmp.length()-1);
	}
	
	private static String getImage(String str,int query1){
		String[] fields=str.split(",");
		String tmp=fields[1];
		if(query1==1)
			return baseSoccerUrl+tmp.substring(1, tmp.length()-1);
		else return baseSnookerUrl+tmp.substring(1, tmp.length()-1);
	}
	
	private static int getClickNum(String str){
		String[] fields=str.split(",");
		String tmp=fields[3];
		return Integer.parseInt(tmp);
	}
	
	private static String getDuration(String str){
		String[] fields=str.split(",");
		String tmp=fields[7];
		int len=Integer.parseInt(tmp);
		int min=len/60;
		int second=len%60;
		int hour=0;
		if(min>=60){
			hour=min/60;
			min=min%60;
		}
		return hour>0? hour+":"+min+":"+second : min+":"+second;
	}
	
	private static String getOrigin(String str){
		String[] fields=str.split(",");
		String tmp=fields[5];
		return tmp.substring(1, tmp.length()-1);
	}
	
	private static String getVideoUrl(String str){
		String[] fields=str.split(",");
		String tmp=fields[fields.length-1];
		return tmp.substring(1, tmp.length()-1);
	}
	
	
}
