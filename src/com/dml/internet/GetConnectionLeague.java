package com.dml.internet;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dml.bean.VideoBean;
import com.dml.cluster.League;
import com.dml.utils.TimeFormat;

public class GetConnectionLeague {
	public static List<VideoBean> getCon(int id, int start){
    	String path = null;
    	JSONArray ja = null;
    	JSONObject jo = null;
    	List<VideoBean> data = new ArrayList<VideoBean>();
         League l=new League();
         
        try {
		    String result=l.getLeagueInfo(id, start);
			
			 jo = new JSONObject(result);  
			 ja =jo.getJSONArray("League");
		}catch(Exception e){
			e.printStackTrace();
		}	
        
		for(int i = 0;i < ja.length(); i++){
			VideoBean video = new VideoBean();
			try{
				video.setId(ja.getJSONObject(i).getString("videoID"));
				video.setImageUrl(ja.getJSONObject(i).getString("imageUrl"));;
				video.setClickNum(ja.getJSONObject(i).getInt("clicknum"));
				video.setLength(TimeFormat.format(ja.getJSONObject(i).getInt("totalduration")));
				video.setOrigin(ja.getJSONObject(i).getString("videoorigin"));
				video.setVideoUrl(ja.getJSONObject(i).getString("videoUrl"));
				video.setTime(ja.getJSONObject(i).getString("videodateCome"));
				video.setTitle(ja.getJSONObject(i).getString("videoTitle"));   
			}catch(Exception e){
				e.printStackTrace();
			}
			data.add(video);
		}
		return data;
    }
}
