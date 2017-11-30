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
import com.dml.utils.TimeFormat;

public class GetConnectionMore {
	
    public static List<VideoBean> getCon(int videotag, int start){
    	String path = null;
    	JSONArray ja = null;
    	JSONObject jo = null;
    	List<VideoBean> data = new ArrayList<VideoBean>();
    	switch(videotag){
    	case 1:
    		path="http://202.114.18.96:8080/SouQiuNet/FootBall_More_Servlet"; 	    
    		break;
    	case 2:
    		path="http://202.114.18.96:8080/SouQiuWang/Basket_More_Servlet"; 
    		break;
    	case 3:
    		 path="http://202.114.18.96:8080/SouQiuWang/Tennis_More_Servlet";
    		 break;
    	case 4:
    		path="http://202.114.18.96:8080/SouQiuWang/TableTennis_More_Servlet"; 
    		break;
    	case 5:
    		path="http://202.114.18.96:8080/SouQiuWang/Badmintain_More_Servlet";
    		break;
    	case 6:	
    		path="http://202.114.18.96:8080/SouQiuWang/MoreVideoServlet";
    		break;
    	case 7:
    	    path="http://202.114.18.96:8080/SouQiuNet/Snooker_More_Servlet";	
    	    break;
    	default:
    	    break;
    	}
    
        try {
		    StringBuilder result=new StringBuilder();
			URL url=new URL(path);
			HttpURLConnection conn=(HttpURLConnection)url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
			conn.setConnectTimeout(5000);
			OutputStream out=conn.getOutputStream();
			DataOutputStream obj=new DataOutputStream(out);
			obj.writeInt(start);
			obj.flush();
			obj.close(); 
			InputStreamReader in=new InputStreamReader(conn.getInputStream(),"UTF-8");
			BufferedReader buff=new BufferedReader(in);
		    String line="";
		    while((line=buff.readLine())!=null){
				 result.append(line+"\n");
			 }
		     in.close();
			 conn.disconnect();
			
			 jo = new JSONObject(result.toString());  
			 ja =jo.getJSONArray("More");
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
