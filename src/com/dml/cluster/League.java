package com.dml.cluster;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;

public class League {

  String path="http://202.114.18.96:8080/SouQiuWang/League";
   
  
	
    public String getLeagueInfo(int id,int start){
    	
    	String result="";
    	  try {
  		    
  			URL url=new URL(path);
  			HttpURLConnection conn=(HttpURLConnection)url.openConnection();
  			 conn.setDoOutput(true);
  			 conn.setDoInput(true);
  			 conn.setRequestMethod("POST");
  			 OutputStream out=conn.getOutputStream();
  			 DataOutputStream obj=new DataOutputStream(out);
  			 String params="id="+URLEncoder.encode(id+"","utf-8")+						
					"&start="+URLEncoder.encode(start+"","utf-8");
		     obj.writeBytes(params); 
  			 obj.flush();
  			 obj.close();
  			 InputStreamReader in=new InputStreamReader(conn.getInputStream());
  			 BufferedReader buff=new BufferedReader(in);
  			 
  			 String line="";
  			 while((line=buff.readLine())!=null){
  				 result+=line+"\n";
  			 }
  		        in.close();
  			 conn.disconnect();	
  		    if(result==null||result.length()==0)
  				 return null;
  			
  			
    	  }catch(Exception e){
    		  
    		  e.printStackTrace();
    	  }
    	  
		return result;
    	
    } 
}
